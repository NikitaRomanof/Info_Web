TRUNCATE TABLE xp CASCADE;
TRUNCATE TABLE p2p CASCADE;
TRUNCATE TABLE checks CASCADE;
TRUNCATE TABLE friends CASCADE;
TRUNCATE TABLE peers CASCADE;
TRUNCATE TABLE recommendations CASCADE;
TRUNCATE TABLE tasks CASCADE;
TRUNCATE TABLE time_tracking CASCADE;
TRUNCATE TABLE transferred_points CASCADE;
TRUNCATE TABLE verter CASCADE;

INSERT INTO peers (nickname, birthday)
VALUES ('A_peer', '1990-01-01'),
       ('B_peer', '1991-11-01'),
       ('C_peer', '1993-03-03'),
       ('D_peer', '1994-04-04'),
       ('E_peer', '1995-05-05'),
       ('F_peer', '1996-06-06'),
       ('G_peer', '1997-07-07'),
       ('H_peer', '1998-08-08');


INSERT INTO tasks (title, parent_task, max_xp)
VALUES ('C2_SimpleBashUtils', NULL, 250),
       ('C3_s21_stringplus', 'C2_SimpleBashUtils', 500),
       ('C5_s21_decimal', 'C3_s21_stringplus', 350),
       ('C6_s21_matrix', 'C5_s21_decimal', 200),
       ('C7_SmartCalc_v1.0', 'C6_s21_matrix', 500),
       ('C8_3DViewer_v1.0', 'C7_SmartCalc_v1.0', 750),
       ('CPP1_s21_matrixplus', 'C8_3DViewer_v1.0', 300),
       ('D01_Linux', 'C2_SimpleBashUtils', 300);


INSERT INTO checks (peer, task, date)
VALUES ('A_peer', 'C2_SimpleBashUtils', '2021-10-30'),
       ('B_peer', 'C2_SimpleBashUtils', '2021-11-01'),
       ('C_peer', 'C2_SimpleBashUtils', '2021-11-03'),
       ('D_peer', 'C2_SimpleBashUtils', '2021-11-04'),
       ('E_peer', 'C2_SimpleBashUtils', '2021-11-05'),
       ('A_peer', 'C3_s21_stringplus', '2021-11-15'),
       ('B_peer', 'C3_s21_stringplus', '2021-11-15'),
       ('H_peer', 'C2_SimpleBashUtils', '2021-11-15'),
       ('A_peer', 'C5_s21_decimal', '2021-11-25'),
       ('A_peer', 'C6_s21_matrix', '2021-11-26'),
       ('A_peer', 'C7_SmartCalc_v1.0', '2021-12-01'),
       ('A_peer', 'C8_3DViewer_v1.0', '2021-12-10');


INSERT INTO p2p (check_id, checking_peer, state, time)
VALUES (1, 'B_peer', 'Start', '12:00'),
       (1, 'B_peer', 'Success', '12:30'),
       (2, 'C_peer', 'Start', '15:00'),
       (2, 'C_peer', 'Success', '15:30'),
       (3, 'D_peer', 'Start', '19:00'),
       (3, 'D_peer', 'Success', '19:30'),
       (4, 'A_peer', 'Start', '11:00'),
       (4, 'A_peer', 'Failure', '11:30'),
       (5, 'H_peer', 'Start', '10:00'),
       (5, 'H_peer', 'Success', '11:00'),
       (6, 'G_peer', 'Start', '20:25'),
       (6, 'G_peer', 'Success', '21:00'),
       (7, 'A_peer', 'Start', '10:10'),
       (7, 'A_peer', 'Success', '10:40'),
       (8, 'E_peer', 'Start', '12:15'),
       (8, 'E_peer', 'Success', '12:30'),
       (9, 'G_peer', 'Start', '18:00'),
       (9, 'G_peer', 'Success', '18:30'),
       (10, 'B_peer', 'Start', '15:00'),
       (10, 'B_peer', 'Success', '15:30'),
       (11, 'D_peer', 'Start', '16:00'),
       (11, 'D_peer', 'Success', '16:50'),
       (12, 'H_peer', 'Start', '10:00'),
       (12, 'H_peer', 'Success', '11:00');


INSERT INTO verter (check_id, State, Time)
VALUES (1, 'Start', '12:31'),
       (1, 'Success', '12:35'),
       (2, 'Start', '15:31'),
       (2, 'Success', '15:35'),
       (3, 'Start', '19:31'),
       (3, 'Failure', '19:33'),
       (5, 'Start', '11:32'),
       (5, 'Success', '11:40'),
       (6, 'Start', '21:02'),
       (6, 'Success', '21:10'),
       (7, 'Start', '10:41'),
       (7, 'Success', '10:45'),
       (8, 'Start', '12:31'),
       (8, 'Success', '12:33'),
       (9, 'Start', '18:31'),
       (9, 'Success', '18:33'),
       (10, 'Start', '15:31'),
       (10, 'Success', '15:33');


INSERT INTO transferred_points (checking_peer, checked_peer, points_amount)
VALUES ('B_peer', 'A_peer', 2),
       ('A_peer', 'B_peer', 1),
       ('C_peer', 'B_peer', 1),
       ('D_peer', 'C_peer', 1),
       ('A_peer', 'D_peer', 1),
       ('H_peer', 'E_peer', 1),
       ('G_peer', 'A_peer', 2),
       ('A_peer', 'H_peer', 1),
       ('H_peer', 'A_peer', 1),
       ('E_peer', 'H_peer', 1),
       ('B_peer', 'C_peer', 1),
       ('C_peer', 'D_peer', 1),
       ('A_peer', 'G_peer', 2);


INSERT INTO friends (peer1, peer2)
VALUES ('A_peer', 'G_peer'),
       ('A_peer', 'D_peer'),
       ('G_peer', 'C_peer'),
       ('F_peer', 'B_peer'),
       ('H_peer', 'E_peer');

INSERT INTO recommendations (peer, recommended_peer)
VALUES ('A_peer', 'B_peer'),
       ('B_peer', 'A_peer'),
       ('B_peer', 'C_peer'),
       ('C_peer', 'D_peer'),
       ('D_peer', 'A_peer'),
       ('E_peer', 'H_peer'),
       ('A_peer', 'G_peer'),
       ('H_peer', 'A_peer'),
       ('A_peer', 'H_peer'),
       ('H_peer', 'E_peer'),
       ('C_peer', 'B_peer'),
       ('D_peer', 'C_peer'),
       ('G_peer', 'A_peer');

INSERT INTO xp (check_id, xp_amount)
VALUES (1, 250),
       (2, 250),
       (5, 250),
       (6, 500),
       (7, 500),
       (8, 250),
       (9, 350),
       (10, 200),
       (11, 500),
       (12, 750);


INSERT INTO time_tracking (peer, date, time, state)
VALUES ('C_peer', '2022-10-09', '18:32', 1),
       ('C_peer', '2022-10-09', '19:32', 2),
       ('C_peer', '2022-10-09', '20:32', 1),
       ('C_peer', '2022-10-09', '22:32', 2),
       ('D_peer', '2022-10-09', '10:32', 1),
       ('D_peer', '2022-10-09', '12:32', 2),
       ('D_peer', '2022-10-09', '13:02', 1),
       ('D_peer', '2022-10-09', '21:32', 2),
       ('E_peer', '2022-05-09', '10:32', 1),
       ('E_peer', '2022-05-09', '12:32', 2),
       ('F_peer', '2022-06-09', '11:02', 1),
       ('F_peer', '2022-06-09', '21:32', 2),
       ('A_peer', '2022-09-21', '15:00', 1),
       ('A_peer', '2022-09-21', '22:00', 2),
       ('B_peer', '2022-09-21', '08:00', 1),
       ('B_peer', '2022-09-21', '20:00', 2),
       ('D_peer', '2022-09-21', '12:00', 1),
       ('D_peer', '2022-09-21', '19:00', 2),
       ('G_peer', '2022-09-21', '18:32', 1),
       ('B_peer', '2022-10-10', '10:32', 1),
       ('B_peer', '2022-10-10', '19:32', 1),
       ('B_peer', '2022-10-10', '22:32', 2),
       ('A_peer', '2023-02-07', '08:30', 1),
       ('A_peer', '2023-02-07', '12:00', 2),
       ('D_peer', '2023-02-07', '08:30', 1),
       ('D_peer', '2023-02-07', '12:00', 2),
       ('B_peer', '2023-02-06', '10:30', 1),
       ('B_peer', '2023-02-06', '10:50', 2),
       ('B_peer', '2023-02-06', '19:32', 1),
       ('B_peer', '2023-02-06', '22:32', 2);


INSERT INTO procedure_calls
VALUES (1, 'select * from transferred_points()'),
       (2, 'select * from check_peers_xp()'),
       (3, 'select * from tracking(?)'),
       (4, '{call success_percent(?::refcursor)}'),
       (5, '{call change_points(?::refcursor)}'),
       (6, '{call change_points_on_first_foo(?::refcursor)}'),
       (7, '{call top_task_on_date(?::refcursor)}'),
       (8, '{call check_duration(?::refcursor)}'),
       (9, '{call finish_block(?::refcursor, ?::varchar)}'),
       (10, '{call most_recommended(?::refcursor)}'),
       (11, '{call block_stat(?::refcursor, ?::varchar, ?::varchar)}'),
       (12, '{call most_friendly(?::refcursor, ?::int)}'),
       (13, '{call birhday_checks(?::refcursor)}'),
       (14, '{call sum_xp(?::refcursor)}'),
       (15, '{call first_second_but_not_third(?::refcursor, ?::varchar, ?::varchar, ?::varchar)}'),
       (16, '{call previous_tasks(?::refcursor)}'),
       (17, '{call lucky_days(?::refcursor, ?::int)}'),
       (18, '{call max_xp(?::refcursor)}'),
       (19, '{call max_xp_peer(?::refcursor)}'),
       (20, '{call max_time_peer(?::refcursor)}'),
       (21, '{call max_time_spent(?::refcursor, ?::time, ?::int)}'),
       (22, '{call out_of_campus_peers(?::refcursor, ?::int, ?::int)}'),
       (23, '{call last_entered_peer(?::refcursor)}'),
       (24, '{call yesterday_out_peers(?::refcursor, ?::int)}'),
       (25, '{call early_entry(?::refcursor)}');
