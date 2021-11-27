[![Build Status](https://app.travis-ci.com/stanovov/job4j_cinema.svg?branch=master)](https://app.travis-ci.com/stanovov/job4j_cinema)

![](https://img.shields.io/badge/Maven-=_3-red)
![](https://img.shields.io/badge/Java-=_14-orange)
![](https://img.shields.io/badge/Servlet-AFBBF8)
![](https://img.shields.io/badge/JDBC-738bff)
![](https://img.shields.io/badge/PostgerSQL-=_9-blue)
![](https://img.shields.io/badge/JUnit-=_4-yellowgreen)
![](https://img.shields.io/badge/Mockito-brightgreen)
![](https://img.shields.io/badge/Checkstyle-lightgrey)

# job4j_cinema

+ [О проекте](#О-проекте)
+ [Технологии](#Технологии)
+ [Использование](#Использование)
+ [Контакты](#Контакты)

## О проекте

Это простое MVC веб-приложение на сервлетах и JDBC, которое позволяет бронировать места для сеансов в кинотеатре.

## Технологии

+ Сборщик проектов **Maven**;
+ Frontend - **HTML**, **CSS**, **BOOTSTRAP**, **JS**, **AJAX**, **JQUERY**;
+ Backend - **Java 14**, **JDBC**, **Servlet**;
+ Логгирование - **Log4j**, **Slf4j**;
+ Используемая СУБД - **PostgreSQL**;
+ Контейнер сервлетов - **Apache Tomcat**;
+ Непрерывная интеграция - **Travis CI**;
+ Инструмент для анализа стиля кода - **Checkstyle**;

## Использование

Основная страница:

![ScreenShot](images/main_1.png)

В верхней части страницы находится переключатель между сеансами. По центру - все места в зале. Ниже - кнопка 
"Продолжить", которая перенаправляет нас на страницу брони. Для бронирования места необходимо выбрать свободное место и
непосредственно кликнуть на кнопку "Продолжить". Забронированные места невозможно выбрать. А также рядом с ними 
добавлена текстовая подсказка (занято)

---

Если переключиться с помощью верхней панели, то данные зала динамически обновятся с сервера:

![ScreenShot](images/main_2.png)

--- 

На странице брони имеются маски для телефона и электронной почты. Страница после выбора места и сеанса:

![ScreenShot](images/payment_1.png)

---

При попытке "Оплатить" без заполненных полей, будет выдано предупреждение:

![ScreenShot](images/payment_2.png)

---

Если кто-то быстрее нас забронировал выбранное нами место, то при попытке "Оплатить", будет выдано уведомление:

![ScreenShot](images/payment_3.png)

---

При удачной брони будет выдано уведомление и нас перекинет на главную страницу:

![ScreenShot](images/payment_4.png)

## Контакты

Становов Семён Сергеевич

Email: sestanovov@gmail.com

Telegram: [@stanovovss](https://t.me/stanovovss)