INSERT INTO tb_client(name, email, phone, address, password) VALUES('Caroline Nair da Costa', 'carolinenairdacosta@outllok.com', '(53) 99678-1222', 'Rua Alexandre Mendonça, 668', 'qo7w7BN7wX');
INSERT INTO tb_client(name, email, phone, address, password) VALUES('Hugo Vitor Martins', 'hugovitormartins@iclud.com', '(96) 98749-2210', 'Rua F, 370', 'AgZ463qGli');
INSERT INTO tb_client(name, email, phone, address, password) VALUES('Giovanni Henrique Cavalcanti', 'giovannihenriquecavalcanti@megamega.com.br', '(34) 99206-8731', 'Rua Garcia, 865', 'x4bysnwYYh');
INSERT INTO tb_client(name, email, phone, address, password) VALUES('Rafael Miguel Bernardes', 'rafael_miguel_bernardes@cosma.com', '(68) 98547-5574', 'Travessa Tefé, 411', '1dst13mohf');
INSERT INTO tb_client(name, email, phone, address, password) VALUES('Isis Sophia Dias', 'isis_dias@iclud.com', '(27) 98128-4005', 'Rua João Cipreste Filho, 354', 'BDPJO7Q269');

INSERT INTO tb_product(name, price) VALUES ('X-Burger', 12.0);
INSERT INTO tb_product(name, price) VALUES ('X-Salada', 14.0);
INSERT INTO tb_product(name, price) VALUES ('X-Bacon', 16.0);
INSERT INTO tb_product(name, price) VALUES ('X-Egg', 15.0);
INSERT INTO tb_product(name, price) VALUES ('X-Egg Bacon', 17.5);
INSERT INTO tb_product(name, price) VALUES ('X-Tudo', 20.0);
INSERT INTO tb_product(name, price) VALUES ('Misto Quente', 8.0);
INSERT INTO tb_product(name, price) VALUES ('Americano', 10.0);
INSERT INTO tb_product(name, price) VALUES ('Burger Clássico', 18.0);
INSERT INTO tb_product(name, price) VALUES ('Burger Cheddar', 20.0);
INSERT INTO tb_product(name, price) VALUES ('Burger Bacon', 22.0);
INSERT INTO tb_product(name, price) VALUES ('Burger Duplo', 25.0);
INSERT INTO tb_product(name, price) VALUES ('Burger BBQ', 23.0);
INSERT INTO tb_product(name, price) VALUES ('Burger Veggie', 21.0);
INSERT INTO tb_product(name, price) VALUES ('Hot Dog Simples', 9.0);
INSERT INTO tb_product(name, price) VALUES ('Hot Dog Completo', 12.0);
INSERT INTO tb_product(name, price) VALUES ('Hot Dog Duplo', 15.0);
INSERT INTO tb_product(name, price) VALUES ('Hot Dog Especial', 17.0);

INSERT INTO tb_order(id, client_id, created_At, order_status) VALUES (1, 2, TIMESTAMP WITH TIME ZONE '2026-01-12T18:19:20.673204744Z', 'EM_PREPARO');
INSERT INTO tb_order(id, client_id, created_At, order_status) VALUES (2, 3, TIMESTAMP WITH TIME ZONE '2026-01-12T18:19:20.673204744Z', 'EM_PREPARO');
INSERT INTO tb_order(id, client_id, created_At, order_status) VALUES (3, 3, TIMESTAMP WITH TIME ZONE '2026-01-12T18:19:20.673204744Z', 'EM_ENTREGA');
INSERT INTO tb_order(id, client_id, created_At, order_status) VALUES (4, 4, TIMESTAMP WITH TIME ZONE '2026-01-12T18:19:20.673204744Z', 'ENTREGUE');
INSERT INTO tb_order(id, client_id, created_At, order_status) VALUES (5, 5, TIMESTAMP WITH TIME ZONE '2026-01-12T18:19:20.673204744Z', 'CANCELADO');

INSERT INTO tb_order_product(order_id, product_id) VALUES (1, 2);
INSERT INTO tb_order_product(order_id, product_id) VALUES (2, 1);
INSERT INTO tb_order_product(order_id, product_id) VALUES (3, 3);
INSERT INTO tb_order_product(order_id, product_id) VALUES (4, 5);
INSERT INTO tb_order_product(order_id, product_id) VALUES (5, 2);

