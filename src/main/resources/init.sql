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
    parent_task varchar references tasks (title) on delete set null,
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
    recommended_peer varchar not null references peers (nickname) ON DELETE CASCADE
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

-- 2) Написать функцию, которая возвращает таблицу вида: ник пользователя, название проверенного задания,
-- кол-во полученного XP
-- В таблицу включать только задания, успешно прошедшие проверку (определять по таблице Checks).
-- Одна задача может быть успешно выполнена несколько раз. В таком случае в таблицу включать все успешные проверки.

DROP FUNCTION IF EXISTS check_peers_xp;
create or replace function check_peers_xp() returns table (peer varchar, task varchar, xp bigint)
AS $$ BEGIN RETURN QUERY EXECUTE
    'select peer, task, xp.xp_amount as XP from checks
  left join xp on xp.check_id = checks.id
  left join p2p on p2p.check_id = checks.id
  left join verter on verter.check_id = checks.id
  where p2p.state = ''Success'' and (verter.state = ''Success'' or verter.state is Null)';
END;
$$ LANGUAGE plpgsql;

-- 3) Написать функцию, определяющую пиров, которые не выходили из кампуса в течение всего дня
-- Параметры функции: день, например 12.05.2022.
-- Функция возвращает только список пиров.

DROP FUNCTION IF EXISTS tracking;
create or replace function tracking(dt date) returns table (ppeer varchar)
AS $func$
BEGIN
    RETURN QUERY
        select peer
        from time_tracking
        where date = dt
        group by peer
        having SUM(state) = 1;
END;
$func$ LANGUAGE plpgsql;

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

-- 16)
--  Используя рекурсивное обобщенное табличное выражение, для каждой задачи вывести кол-во предшествующих ей задач
-- То есть сколько задач нужно выполнить, исходя из условий входа, чтобы получить доступ к текущей.
-- Формат вывода: название задачи, количество предшествующих

DROP PROCEDURE IF EXISTS previous_tasks;
CREATE OR REPLACE PROCEDURE previous_tasks(result_data INOUT refcursor)
AS $$ BEGIN OPEN result_data FOR
    WITH RECURSIVE r(title, parent_task, n) AS (
        SELECT tasks.title,
               tasks.parent_task,
               0
        FROM tasks
        UNION
        SELECT T.title,
               T.parent_task,
               n + 1
        FROM tasks T
                 INNER JOIN r ON r.title = T.parent_task
    )
    SELECT title AS Task,
           MAX(n) AS PrevCount
    FROM r
    GROUP BY title;
END;
$$ LANGUAGE plpgsql;

-- 17) Найти "удачные" для проверок дни. День считается "удачным", если в нем есть хотя бы N идущих подряд успешных проверки
-- Параметры процедуры: количество идущих подряд успешных проверок N.
-- Временем проверки считать время начала P2P этапа.
-- Под идущими подряд успешными проверками подразумеваются успешные проверки, между которыми нет неуспешных.
-- При этом кол-во опыта за каждую из этих проверок должно быть не меньше 80% от максимального.

DROP PROCEDURE IF EXISTS lucky_days;
CREATE OR REPLACE PROCEDURE lucky_days (result_data INOUT refcursor, N int) AS $$ BEGIN OPEN result_data FOR
    WITH data AS(
        SELECT date,time, status_check, LEAD(status_check) OVER (ORDER BY date, time) AS next_status_check
        FROM ( SELECT checks.date,
                      case WHEN 100 * xp.xp_amount / tasks.max_xp >= 80 THEN true
                           ELSE false
                          END AS status_check, p2p.time
               FROM checks
                        JOIN tasks ON checks.task = tasks.title
                        JOIN xp ON checks.id = xp.check_id
                        JOIN p2p ON checks.id = p2p.check_id
                   AND p2p.state in('Success', 'Failure'))
                 ch), data_prev_checks AS (
        SELECT t1.date, t1.time, t1.status_check, t1.next_status_check, COUNT (t2.date) as success_count
        FROM data t1
                 JOIN data t2 on t1.date = t2.date AND t1.time <= t2.time AND t1.status_check = t2.next_status_check
        GROUP BY t1.date, t1.time, t1.status_check, t1.next_status_check)
    SELECT date
    FROM ( SELECT date, MAX(success_count) AS max_success_count
           FROM ( SELECT date, success_count
                  FROM data_prev_checks
                  WHERE status_check
                ) success_checks
           GROUP BY date) m
    WHERE max_success_count >= N;
END;
$$ LANGUAGE plpgsql;

-- 18) Определить пира с наибольшим числом выполненных заданий
-- Формат вывода: ник пира, число выполненных заданий

DROP PROCEDURE IF EXISTS max_xp;
CREATE OR REPLACE PROCEDURE max_xp(result_data INOUT refcursor)
AS $$ BEGIN OPEN result_data for

    select peer, count(xp_amount) XP from xp
                                             join checks on checks.id = xp.check_id
    group by peer
    order by XP desc limit 1;

END;
$$ LANGUAGE plpgsql;

--  19) Определить пира с наибольшим количеством XP
DROP PROCEDURE IF EXISTS max_xp_peer;
CREATE OR REPLACE PROCEDURE max_xp_peer(result_data INOUT refcursor)
    LANGUAGE plpgsql AS $$
BEGIN
    OPEN result_data FOR
        SELECT peers.nickname, sum(xp.xp_amount) as sum
        FROM peers
                 JOIN checks
                      ON peers.nickname = checks.peer
                 JOIN xp
                      ON checks.id = xp.check_id
        GROUP BY peers.nickname
        ORDER BY sum DESC
        LIMIT 1;
END;
$$;

-- 20) Определить пира, который провел сегодня в кампусе больше
-- всего времени. Формат вывода: ник пира

DROP PROCEDURE IF EXISTS max_time_peer;
CREATE OR REPLACE PROCEDURE max_time_peer(result_data INOUT refcursor)
    LANGUAGE plpgsql AS $$
BEGIN
    OPEN result_data FOR
        WITH startState AS (SELECT peer, time AS inTime, state
                            FROM time_tracking
                            WHERE state = 1
                              AND date = CURRENT_DATE),
             finishState AS (SELECT peer, time AS outTime, state
                             FROM time_tracking
                             WHERE state = 2
                               AND date = CURRENT_DATE)

        SELECT startState.peer
        FROM startState
                 JOIN finishState
                      ON startState.peer = finishState.peer
        ORDER BY finishState.outTime - startState.inTime DESC
        LIMIT 1;
END;
$$;

-- 21) Определить пиров, приходивших раньше заданного времени не менее N раз за всё время
-- Параметры процедуры: время, количество раз N.
-- Формат вывода: список пиров

DROP PROCEDURE IF EXISTS max_time_spent;
CREATE OR REPLACE PROCEDURE max_time_spent(result_data INOUT refcursor, checkedTime time, N integer)
    LANGUAGE plpgsql AS $$
BEGIN
    OPEN result_data FOR
        SELECT peer
        FROM time_tracking
        WHERE state = 1
          AND time < checkedTime
        GROUP BY peer
        HAVING count(peer) > N;
END;
$$;

-- 22) Определить пиров, выходивших за последние N дней из кампуса больше M раз
-- Параметры процедуры: количество дней N, количество раз M.
-- Формат вывода: список пиров

DROP PROCEDURE IF EXISTS out_of_campus_peers;
CREATE OR REPLACE PROCEDURE out_of_campus_peers(result_data INOUT refcursor, daysCount integer, N integer)
    LANGUAGE plpgsql AS $$
BEGIN
    OPEN result_data FOR
        SELECT peer
        FROM time_tracking
        WHERE time_tracking.state = 2
          AND current_date - time_tracking.date <= daysCount
        GROUP BY peer
        HAVING COUNT(peer) > N;
END;
$$;

-- 23) Определить пира, который пришел сегодня последним
-- Формат вывода: ник пира

DROP PROCEDURE IF EXISTS last_entered_peer;
CREATE OR REPLACE PROCEDURE last_entered_peer(result_data INOUT refcursor)
    LANGUAGE plpgsql AS $$
BEGIN
    OPEN result_data FOR
        SELECT peer
        FROM time_tracking
        WHERE date = current_date
          AND state = 1
        ORDER BY time DESC
        LIMIT 1;
END;
$$;

-- 24) Определить пиров, которые выходили вчера из кампуса
-- больше чем на N минут
-- Параметры процедуры: количество минут N.
-- Формат вывода: список пиров

DROP PROCEDURE IF EXISTS yesterday_out_peers;
CREATE OR REPLACE PROCEDURE yesterday_out_peers(result_data INOUT refcursor, minutesCounts integer)
    LANGUAGE plpgsql AS $$
BEGIN
    OPEN result_data FOR
        WITH startState AS (SELECT *
                            FROM time_tracking
                            WHERE state = 1
                              AND date = CURRENT_DATE - 1),
             finishState AS (SELECT *
                             FROM time_tracking
                             WHERE state = 2
                               AND date = CURRENT_DATE - 1)

        SELECT startState.peer
        FROM startState
                 JOIN finishState
                      ON startState.peer = finishState.peer
                          AND finishState.time < startState.time
                          AND startState.time - finishState.time > concat(minutesCounts, 'minutes')::interval;
END;
$$;

-- 25) Определить для каждого месяца процент ранних входов
-- Для каждого месяца посчитать, сколько раз люди, родившиеся в этот месяц,
-- приходили в кампус за всё время (будем называть это общим числом входов).
-- Для каждого месяца посчитать, сколько раз люди, родившиеся в этот месяц,
-- приходили в кампус раньше 12:00 за всё время (будем называть это числом ранних входов).
-- Для каждого месяца посчитать процент ранних входов в кампус относительно общего числа входов.
-- Формат вывода: месяц, процент ранних входов

DROP PROCEDURE IF EXISTS early_entry;
CREATE OR REPLACE PROCEDURE early_entry(result_data INOUT refcursor)
    LANGUAGE plpgsql AS $$
BEGIN
    OPEN result_data FOR
        WITH gs AS (SELECT generate_series(1, 12) as months),
             all_entry AS (SELECT DATE_PART('month', time_tracking.date) as months, COUNT(*) AS counts
                           FROM time_tracking
                                    JOIN peers
                                         ON time_tracking.peer = peers.nickname
                           WHERE time_tracking.state = '1'
                             AND DATE_PART('month', peers.birthday) = DATE_PART('month', time_tracking.date)
                           GROUP BY months),
             early_entry AS (SELECT DATE_PART('month', time_tracking.date) as months, count(*) AS counts
                             FROM time_tracking
                                      JOIN peers
                                           ON time_tracking.peer = peers.nickname
                             WHERE time_tracking.state = '1'
                               AND DATE_PART('month', peers.birthday) = DATE_PART('month', time_tracking.date)
                               AND time_tracking.time < '12:00:00'
                             GROUP BY months)

        SELECT to_char(to_timestamp(entery.months::text, 'MM'), 'MONTH') AS Month,
               entery.count2 * 100 / entery.count1 AS EarlyEntries
        FROM (SELECT gs.months, all_entry.counts as count1,
                     early_entry.counts as count2
              FROM gs
                       JOIN all_entry ON all_entry.months = gs.months
                       JOIN early_entry ON early_entry.months = gs.months
             ) AS entery;
END;
$$;


