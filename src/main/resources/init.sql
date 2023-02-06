drop table if exists peers CASCADE;
drop table if exists tasks CASCADE;
drop table if exists checks CASCADE;
drop table if exists p2p CASCADE;
drop table if exists verter CASCADE;
drop table if exists transferred_points CASCADE;
drop table if exists friends CASCADE;
drop table if exists recommendations CASCADE;
drop table if exists xp CASCADE;
drop table if exists time_tracking CASCADE;
drop table if exists procedure_calls CASCADE;
DROP TYPE IF EXISTS status CASCADE;
DROP CAST IF EXISTS (character varying AS status);

create table if not exists peers
(
    nickname varchar primary key,
    birthday date not null
);

create table if not exists tasks
(
    title       varchar primary key,
    parent_task varchar references tasks (title) on delete cascade,
    max_xp      bigint NOT NULL
);

create type status as enum ('Start', 'Success', 'Failure');
CREATE CAST (character varying AS status) WITH INOUT AS ASSIGNMENT;

create table if not exists checks
(
    id   serial primary key,
    peer varchar not null references peers (nickname) ON DELETE CASCADE,
    task varchar not null references tasks (title) ON DELETE CASCADE,
    date date
);

create table if not exists p2p
(
    id            serial primary key,
    check_id      bigint  not null references checks (id) ON DELETE CASCADE,
    checking_peer varchar not null references peers (nickname) ON DELETE CASCADE,
    state         status,
    time          time
);

create table if not exists verter
(
    id       serial primary key,
    check_id bigint not null references checks (id) ON DELETE CASCADE,
    State    status,
    time     time
);

create table if not exists transferred_points
(
    id            serial primary key,
    checking_peer varchar not null references peers (nickname) ON DELETE CASCADE,
    checked_peer   varchar not null references peers (nickname) ON DELETE CASCADE,
    points_amount  bigint  not null default 1,
    check (checking_peer != checked_peer)
);

create table if not exists friends
(
    id    serial primary key,
    peer1 varchar not null REFERENCES peers (nickname) ON DELETE CASCADE,
    peer2 varchar not null REFERENCES peers (nickname) ON DELETE CASCADE,
    check (peer1 != peer2)
);

create table if not exists recommendations
(
    id              serial primary key,
    peer            varchar not null references peers (nickname) ON DELETE CASCADE,
    recommended_peer varchar default null references peers (nickname) ON DELETE SET NULL,
    check (peer != recommended_peer),
    unique (peer, recommended_peer)
);

create table if not exists xp
(
    id       serial primary key,
    check_id bigint not null references checks (id) ON DELETE CASCADE,
    xp_amount bigint
);

create table if not exists time_tracking
(
    id    serial primary key,
    peer  varchar not null references peers (nickname) ON DELETE CASCADE,
    date  date,
    time  time,
    state int check (State in (1, 2))
);

create table if not exists procedure_calls
(
    id  bigint primary key,
    call  varchar not null
);

-- 1) Написать функцию, возвращающую таблицу TransferredPoints в более человекочитаемом виде
-- Ник пира 1, ник пира 2, количество переданных пир поинтов.
-- Количество отрицательное, если пир 2 получил от пира 1 больше поинтов.

DROP FUNCTION IF EXISTS transferred_points;
CREATE OR REPLACE FUNCTION transferred_points () RETURNS TABLE (peer1 varchar, peer2 varchar, points_amount numeric)
AS $$ BEGIN RETURN QUERY EXECUTE
'with t1 as (select checking_peer as peer1,
checked_peer as peer2,
SUM(points_amount) as points_amount
from transferred_points
GROUP by peer1, peer2)

select tt1.peer1, tt1.peer2, (tt1.points_amount - tt2.points_amount) as points_amount
from t1 tt1, t1 tt2
where (tt1.peer1 = tt2.peer2) and (tt1.peer2 = tt2.peer1) and tt1.peer1 < tt2.peer1
union all
(select tt1.peer1, tt1.peer2, tt1.points_amount
from t1 tt1
except
select tt1.peer1, tt1.peer2, tt1.points_amount
from t1 tt1, t1 tt2
where (tt1.peer1 = tt2.peer2) and (tt1.peer2 = tt2.peer1))
order by peer1, peer2';
END;
$$ LANGUAGE plpgsql;

-- 4) Найти процент успешных и неуспешных проверок за всё время
-- Формат вывода: процент успешных, процент неуспешных
DROP PROCEDURE IF EXISTS success_percent;
CREATE OR REPLACE PROCEDURE success_percent(result_data INOUT refcursor)
AS $$ BEGIN OPEN result_data FOR with ff as (
select id, check_id, state, time from p2p
where not (state = 'Start')
union all
select id, check_id,state, time from verter
where not (state = 'Start'))
select (cast(cast((select count(*)
from p2p
where not (state = 'Start')) - count(*) as numeric) / (select count(*)
from p2p
where not (state = 'Start')) * 100 as int)) AS successful_checks ,
cast(cast(count(*) as numeric) / (select count(*)
from p2p
where not (state = 'Start')) * 100 as int) AS unsuccessful_checks
from ff
where (state = 'Failure');
END;
$$ LANGUAGE plpgsql;

-- 5) Посчитать изменение в количестве пир поинтов каждого пира по таблице TransferredPoints
-- Результат вывести отсортированным по изменению числа поинтов.
-- Формат вывода: ник пира, изменение в количество пир поинтов
DROP PROCEDURE IF EXISTS change_points;
CREATE OR REPLACE PROCEDURE change_points (result_data INOUT refcursor)
AS $$ BEGIN OPEN result_data FOR
select checking_peer as peer, SUM(points_amount) as points_change
from
(
SELECT checking_peer, SUM(points_amount) as points_amount
FROM transferred_points
group by checking_peer
union all
SELECT checked_peer, SUM(-points_amount) as points_amount
FROM transferred_points
group by checked_peer) as change
group by peer
order by peer;
END;
$$ LANGUAGE plpgsql;

-- 6) Посчитать изменение в количестве пир поинтов каждого пира по таблице, возвращаемой первой функцией из Part 3
-- Результат вывести отсортированным по изменению числа поинтов.
-- Формат вывода: ник пира, изменение в количество пир поинтов
DROP procedure IF EXISTS change_points_on_first_foo;
CREATE OR REPLACE procedure change_points_on_first_foo (result_data INOUT refcursor)
AS $$ BEGIN OPEN result_data FOR
select peer1 as peer, sum(points_amount) as points_change
from
(select peer1, SUM(points_amount) as points_amount
from transferred_points()
group by peer1
union all
select peer2, SUM(-points_amount) as points_amount
from transferred_points()
group by peer2) as change
group by peer
order by peer;
END;
$$ LANGUAGE plpgsql;

-- 7) Определить самое часто проверяемое задание за каждый день
-- При одинаковом количестве проверок каких-то заданий в определенный день, вывести их все.
-- Формат вывода: день, название задания
DROP procedure IF EXISTS top_task_on_date;
create or replace procedure top_task_on_date(result_data INOUT refcursor)
AS $$ BEGIN OPEN result_data FOR
  WITH t1 AS (
    SELECT date, checks.task, COUNT(task) AS count_task
    FROM checks
    GROUP BY checks.task, date
)
SELECT date AS day, t2.task
FROM (
    SELECT t1.task, t1.date, rank() OVER (PARTITION BY t1.date ORDER BY count_task DESC) AS rank
    FROM t1
    ) AS t2
WHERE rank = 1
ORDER BY day;
END;
$$ LANGUAGE plpgsql;

-- 8) Определить длительность последней P2P проверки
-- Под длительностью подразумевается разница между временем, указанным в записи со статусом "начало", и временем, указанным в записи со статусом "успех" или "неуспех".
-- Формат вывода: длительность проверки
DROP PROCEDURE IF EXISTS check_duration;
CREATE OR REPLACE PROCEDURE check_duration(result_data INOUT refcursor)
AS $$ BEGIN OPEN result_data FOR


(with t1 as (select * from p2p
where id = (SELECT max(id)
            FROM p2p) or id = (SELECT max(id)FROM p2p) - 1)
select tt2.time - tt1.time as duration_time from t1 tt1, t1 tt2
where tt2.id = (SELECT max(id) FROM t1) and tt1.id = (SELECT max(id) FROM t1) - 1);
END;
$$ LANGUAGE plpgsql;

-- 9) Найти всех пиров, выполнивших весь заданный блок задач и дату завершения последнего задания
-- Параметры процедуры: название блока, например "CPP".
-- Результат вывести отсортированным по дате завершения.
-- Формат вывода: ник пира, дата завершения блока (т.е. последнего выполненного задания из этого блока)
DROP PROCEDURE IF EXISTS finish_block;
CREATE OR REPLACE PROCEDURE finish_block(result_data INOUT refcursor, in_block IN varchar)
AS $$ BEGIN OPEN result_data for

select checks.peer, date  from checks
full join p2p on p2p.check_id = checks.id
full join verter on verter.check_id = checks.id
where p2p.state = 'Success' and  (verter.state = 'Success' or verter.state is null)
and checks.task = (
select title
from (select title, substring(tasks.title from '^[A-Z]*') as block
from tasks) as t1
where t1.block = in_block
order by title DESC
limit 1);
END;
$$ LANGUAGE plpgsql;


-- 10) Определить, к какому пиру стоит идти на проверку каждому обучающемуся
-- Определять нужно исходя из рекомендаций друзей пира, т.е. нужно найти пира, проверяться у которого рекомендует наибольшее число друзей.
-- Формат вывода: ник пира, ник найденного проверяющего
DROP PROCEDURE IF EXISTS most_recommended;
CREATE OR REPLACE PROCEDURE most_recommended(result_data INOUT refcursor)
AS $$ BEGIN OPEN result_data for
select peer1, recommended_peer from
(with t1 as (select peer1, peer2 as friend from friends
union all
select peer2, peer1 as friend from friends
)
select distinct ON(peer1)peer1,
recommended_peer,
COUNT(friend) as num

from t1
full join recommendations on t1.friend = recommendations.peer
where peer1 != recommended_peer
group by peer1, recommended_peer
order by peer1, num desc) as tt2;
END;
$$ LANGUAGE plpgsql;

-- 11) Определить процент пиров, которые:

-- Приступили к блоку 1
-- Приступили к блоку 2
-- Приступили к обоим
-- Не приступили ни к одному

-- Параметры процедуры: название блока 1, например CPP, название блока 2, например A.
-- Формат вывода: процент приступивших к первому блоку, процент приступивших ко второму блоку, процент приступивших к обоим, процент не приступивших ни к одному
DROP PROCEDURE IF EXISTS block_stat;
CREATE OR REPLACE PROCEDURE block_stat(result_data INOUT refcursor,
block1 IN varchar, block2 IN varchar)
AS $$
BEGIN OPEN result_data for
with t1 as
(select *
from
(select distinct on (peer) peer, title, substring(tasks.title from '^[A-Z]*') as block
from tasks
join checks ch on ch.task = title) as t1
where block = block1),
t2 as (select *
from
(select distinct on (peer) peer, title, substring(tasks.title from '^[A-Z]*') as block
from tasks
join checks ch on ch.task = title) as t1
where block = block2),
t3 as (select * from (select *
from t2
intersect
select *
from t1) as inter),
t4 as (select nickname as peer from peers
	  except
	  (select peer from t1 union select peer from t2)),
total as (select count(*) from peers),
first_col as (select (count(*) * 100 / (select * from total)) as started_block1
from t1),
second_col as (select (count(*) * 100 / (select * from total)) as started_block2
from t2),
third_col as (select (count(*) * 100 / (select * from total)) as started_both_blocks
from t3),
fourth_col as (select (count(*) * 100 / (select * from total)) as didnt_start_any_block
from t4)
select *
from first_col fc, second_col sc, third_col tc, fourth_col frc;
END;
$$ LANGUAGE plpgsql;


-- 12) Определить N пиров с наибольшим числом друзей
-- Параметры процедуры: количество пиров N.
-- Результат вывести отсортированным по кол-ву друзей.
-- Формат вывода: ник пира, количество друзей

DROP PROCEDURE IF EXISTS most_friendly;
CREATE OR REPLACE PROCEDURE most_friendly(result_data INOUT refcursor, num int)
AS $$ BEGIN OPEN result_data for
select peer1, count(peer2)
from friends
group by peer1
limit num;
END;
$$ LANGUAGE plpgsql;

-- 13) Определить процент пиров, которые когда-либо успешно проходили проверку в свой день рождения
-- Также определите процент пиров, которые хоть раз проваливали проверку в свой день рождения.
-- Формат вывода: процент успехов в день рождения, процент неуспехов в день рождения

DROP PROCEDURE IF EXISTS birhday_checks;
CREATE OR REPLACE PROCEDURE birhday_checks(result_data INOUT refcursor)
AS $$ BEGIN OPEN result_data for

select
((select count(peer) as successful_checks from checks
full join peers on peers.nickname = checks.peer
full join p2p on p2p.check_id = checks.id
full join verter on verter.check_id = checks.id
where extract(day from checks.date ) = extract(day from peers.birthday)
            and
      extract(month from checks.date) = extract(month from peers.birthday) and
      p2p.state = 'Success' and (Verter.state = 'Success' or Verter.state is null)) * 100) / count(peers.nickname) as successful_checks,

      ((select count(peer) as unsuccessful_checks from checks
full join peers on peers.nickname = checks.peer
full join p2p on p2p.check_id = checks.id
full join verter on verter.check_id = checks.id
where extract(day from checks.date ) = extract(day from peers.birthday)
            and
      extract(month from checks.date) = extract(month from peers.birthday) and
      p2p.state = 'Failure' and (Verter.state = 'Failure' or Verter.state is null)) * 100) / count(peers.nickname) as unsuccessful_checks
from peers;

END;
$$ LANGUAGE plpgsql;

-- 14) Определить кол-во XP, полученное в сумме каждым пиром
-- Если одна задача выполнена несколько раз, полученное за нее кол-во XP равно максимальному за эту задачу.
-- Результат вывести отсортированным по кол-ву XP.
-- Формат вывода: ник пира, количество XP
DROP PROCEDURE IF EXISTS sum_xp;
CREATE OR REPLACE PROCEDURE sum_xp(result_data INOUT refcursor)
AS $$ BEGIN OPEN result_data for

select peer, SUM(xp_amount) AS xp from (
select peer, task, MAX(xp_amount) AS xp_amount
from xp
 join checks on checks.id = xp.check_id
 group by peer, task
) as xpp
group by peer
order by XP DESC;

END;
$$ LANGUAGE plpgsql;

-- 15) Определить всех пиров, которые сдали заданные задания 1 и 2, но не сдали задание 3
-- Параметры процедуры: названия заданий 1, 2 и 3.
-- Формат вывода: список пиров
DROP PROCEDURE IF EXISTS first_second_but_not_third;
CREATE OR REPLACE PROCEDURE first_second_but_not_third(result_data INOUT refcursor,
task1 IN varchar, task2 IN varchar, task3 IN varchar)
AS $$
BEGIN OPEN result_data for
with raw_data as
(select ch.peer, task
from p2p
join verter v on v.check_id = p2p.check_id
join checks ch on ch.id = p2p.check_id
where v.state = 'Success' and p2p.state = 'Success')
select peer
from raw_data
where task = task1
intersect
select peer
from raw_data
where task = task2
except
select peer
from raw_data
where task = task3;
END;
$$ LANGUAGE plpgsql;
