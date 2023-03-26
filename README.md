# Info21 Web

Создание web-интерфейса для проекта SQL_Info2_21 на языке Java с использование spring boot, Spring Data JPA, Thymeleaf, Bootstrap. Приложение поддерживаeт осуществление CRUD-операций, импорт/экспорт таблиц, осуществление операций/функций через графический интерфейс, а также логирование действий пользователя.

### Общие требования
- Программа разработана на языке Java версии 11
- Код программы находится в папке src
- Программа реализована с использованием MVC-фреймворка (Spring)
- Рендеринг страниц осуществлять на стороне сервера (использование технологии **Server-Side Rendering**)

### Требования к содержанию

- Главная страница содержит:
    - Навигационное меню, обеспечивающее переход к основным разделам приложения: *«Данные»* и *«Операции»*
    
- Графическая оболочка страниц *«Данные»* и *«Операции»* содержит следующие разделы:
    - Шапка, по нажатию на которую можно осуществить переход на главную страницу
    - Навигационное меню, позволяющее осуществить переход по основным разделам
    - Навигационная панель, осуществляющая передвижение по подразделам выбранного раздела (в случае необходимости)

- Раздел *«Данные»* содержит подразделы, которые позволяют через GUI поддерживать следующий функционал:
    - Совершать CRUD-операции по всем таблицам
    - При любой модификации таблиц (create, update, delete) приложение запрашивает у пользователя подтверждение на осуществление операции
    - После любого вида модификации таблиц пользователю выводится измененная таблица
    - Импорт и экспорт данных для каждой таблицы из файлов/в файлы с расширением *.csv*

- Раздел *«Операции»* содержит компоненты:
    - Блок, содержащий все возможные для вызова запросы из проекта SQL_Info2_21, наименование/краткое описание сути запроса
    - Блок с возможностью самостоятельного ввода SQL-запроса пользователем

- Раздел *«Операции»* содержит подразделы, которые позволяют через GUI поддерживать следующий функционал:
    - Выбор желаемой процедуры / функции / запроса из разработанных в проекте SQL2 с выводом результата и возможность экспорта результата в файл разрешения .csv
    - В случае необходимости введения параметров для выполнения процедуры или функции, графический интерфейс предоставляет форму для ввода данных
    - Если введенные аргументы/SQL-запрос были некорректны, то приложение обрабатывает подобную ситуацию (выдаёт ошибку о некорректности введенных данных и предлагает повторить попытку ввода).
    
    ![Запись экрана 2023-03-26 в 11 08 51 AM](https://user-images.githubusercontent.com/66296571/227755461-664cdd50-8030-4944-914c-bae7463302c5.gif)
