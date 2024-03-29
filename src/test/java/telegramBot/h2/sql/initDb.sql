CREATE TABLE IF NOT EXISTS Exchange(id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                    exchange_name character varying(20), exchange_link text);

INSERT INTO Exchange (exchange_name, exchange_link) VALUES ('HabrFreelance', 'https://freelance.habr.com');
INSERT INTO Exchange (exchange_name, exchange_link) VALUES ('Fl.ru', 'https://www.fl.ru');
INSERT INTO Exchange (exchange_name, exchange_link) VALUES ('Kwork', 'https://kwork.ru');

CREATE INDEX IF NOT EXISTS exchange_name_index ON Exchange(exchange_name);

CREATE TABLE IF NOT EXISTS Subscription(id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                        lang character varying(20), status character varying(6));

INSERT INTO Subscription (lang, status) VALUES ('JavaScript', 'CREATE');
INSERT INTO Subscription (lang, status) VALUES ('Python', 'CREATE');
INSERT INTO Subscription (lang, status) VALUES ('Java', 'CREATE');
INSERT INTO Subscription (lang, status) VALUES ('Ruby', 'CREATE');
INSERT INTO Subscription (lang, status) VALUES ('PHP', 'CREATE');
INSERT INTO Subscription (lang, status) VALUES ('C', 'CREATE');

CREATE INDEX IF NOT EXISTS lang_index ON Subscription(lang);

CREATE TABLE IF NOT EXISTS Orders(id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                  order_title text, order_link text, init_date date,
                                  exchange_id int, subscription_id int,
                                  FOREIGN KEY (exchange_id) REFERENCES Exchange(id),
                                  FOREIGN KEY (subscription_id) REFERENCES Subscription(id));

INSERT INTO Orders(order_title, order_link, init_date, subscription_id, exchange_id)
VALUES ('Разработать VPN приложение', '/tasks/558325', now(), 2, 1);

INSERT INTO Orders(order_title, order_link, init_date, subscription_id, exchange_id)
VALUES ('Доработать скрипт на python',
        '/projects/5290160/dorabotat-skript-na-python.html', now(), 2, 2);

INSERT INTO Orders(order_title, order_link, init_date, subscription_id, exchange_id)
VALUES ('Верстка сайта', '/projects/2366197', now(), 1, 3);

CREATE INDEX IF NOT EXISTS order_link_index ON Orders(order_link);

CREATE TABLE IF NOT EXISTS Users(id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                 chat_id character varying(30), active boolean, user_role character varying (5));

CREATE TABLE IF NOT EXISTS Users_subscriptions(user_id int, subscription_id int, PRIMARY KEY(user_id, subscription_id),
                                               CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES Users(id),
                                               CONSTRAINT fk_subscription FOREIGN KEY(subscription_id) REFERENCES Subscription(id));
