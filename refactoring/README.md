## Лабораторная 3

Цель: получить практический опыт применения техник рефакторинга.

Скачайте приложение: https://github.com/akirakozov/software-design/tree/master/java/refactoring

Приложение представляет собой простой web-server, который хранит информацию о товарах и их цене. Поддержаны такие
методы:

* `http://localhost:8081/get-products` - посмотреть все товары в базе
* `http://localhost:8081/add-product?name=iphone6&price=300` - добавить новый товар
* `http://localhost:8081/query?command=sum` - выполнить некоторый запрос с данными в базе

Необходимо отрефакторить этот код (логика методов не должна измениться), например:

* убрать дублирование
* выделить отдельный слой работы с БД
* выделить отдельный слой формирования html-ответа
* и т.д.

Указание:

* задание нужно сдавать через e-mail, в заголовке письма указать “[SD-TASK]”
* проект перенести к себе github.com
* сначала добавить тесты (отдельными комитами)
* каждый отдельный рефакторинг делать отдельным комитом
* без истории изменений и тестов баллы буду сниматься