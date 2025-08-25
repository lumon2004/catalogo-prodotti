-- Creazione sequenza per la tabella utente
/*ALTER TABLE utente ALTER COLUMN id SET DEFAULT nextval('utente_id_seq');
ALTER TABLE prodotto ALTER COLUMN id SET DEFAULT nextval('prodotto_id_seq');
ALTER TABLE commento ALTER COLUMN id SET DEFAULT nextval('commento_id_seq');*/

-- Svuota le tabelle esistenti e resetta le sequenze
/* TRUNCATE TABLE prodotti_simili RESTART IDENTITY CASCADE;
TRUNCATE TABLE commento RESTART IDENTITY CASCADE;
TRUNCATE TABLE prodotto RESTART IDENTITY CASCADE;
TRUNCATE TABLE utente RESTART IDENTITY CASCADE;*/

-- Inserimento utenti
/*INSERT INTO utente (username, email, password, role) VALUES
('admin', 'luc.monaco8@stud.uniroma3.it', '$2a$10$XxFgdO4/vmUedCcvtgYFGeB.vdWLNH2//Qd96ew7NMaWVEK77PMdC', 'ADMIN'),
('franco', 'franco@pippo.it', '$2a$10$7LZ739Jbg6QLkU0Wm28XA.PwdUgMHU63ARM9MDk2CSR7/Mt2v1gEG', 'USER'),
('mario_rossi', 'mario.rossi@gmail.com', '$2a$10$z2tVVdyyAVbvFyBAwWM3e.EPdKf3EUsMuJI1ISoG3VMo3IIpZaZEy', 'USER'),
('giulia_bianchi', 'giulia.bianchi@outlook.it', '$2a$10$H71lU.rdxz/baxCTfUwbHej8.hg1Dt4fbg2BmtmxUPPf/gQ4YTif6', 'USER'),
('luca_ferrari', 'luca.ferrari@yahoo.com', '$2a$10$RkoDJYpvYj5j2BYrViJIH.nHnNuK0qjFn9eNBnBbwmbWrXXTe0R9C', 'USER'),
('francesca_romano', 'f.romano@libero.it', '$2a$10$DXhIyzdJEcGTkqQjh3zkZuXjWLP36Kpo5qmSf08bS2WPkkU3a7DtC', 'USER'),
('alessandro_costa', 'alex.costa@hotmail.com', '$2a$10$szK/CzN3YRgcOcBH0XKQe.ZLnLqNz5unruMMKffVKtDgWUcY2N47a', 'USER'),
('sofia_ricci', 'sofia.ricci@gmail.com', '$2a$10$vkgmoLOtnO5YOHsoiaeEueV5MOv9RowOwKsHQYTcrhjMtXhFfixBi', 'USER'),
('davide_moretti', 'd.moretti@virgilio.it', '$2a$10$YHHAv0LC91ftOAFdmjs9Pu547zGMh3utiaZCV2N3x76rFqVeQcvGW', 'USER'),
('valentina_greco', 'vale.greco@tiscali.it', '$2a$10$9jNCBSVcbonbub7coQxBbeOACxQDu/CRiDVFXlo/dE18DV4jJRbfu', 'USER'),
('matteo_conti', 'matteo.conti@protonmail.com', '$2a$10$WCg1jlrYOlavKPzl0mu2w.fO5nCQ2lL2KecZfIXenpoII0/hPdgMS', 'USER'),
('chiara_gallo', 'chiara.gallo@fastwebnet.it', '$2a$10$VlWiUIjlvw8Zu6ICOOXZjOJOgwI8ZpvPcZ2KNfgH0uj.cUD94oPyO', 'USER'),
('simone_barbieri', 's.barbieri@gmail.com', '$2a$10$D9TlHRqtAs6ukenFdLKs7uG2ttvvtAkVkRj4ZXq7A3oxw3ONJnriu', 'USER'),
('elena_marchetti', 'elena.marchetti@icloud.com', '$2a$10$gdsJPtnLrTIgYI3/WNPxb.UTqlx2RUirvPHUkguxasbbyT.Rqpkje', 'USER');

-- Inserimento prodotti
INSERT INTO prodotto (nome, prezzo, tipologia, descrizione) VALUES 
('iPhone 14', 899.99, 'SMARTPHONE', 'Smartphone Apple con chip A15 Bionic, fotocamera da 12MP e display Super Retina XDR da 6.1 pollici'),
('Samsung Galaxy S23', 849.99, 'SMARTPHONE', 'Smartphone Android con processore Snapdragon 8 Gen 2, tripla fotocamera e display AMOLED da 6.1 pollici'),
('MacBook Air M2', 1399.99, 'LAPTOP', 'Laptop ultraleggero con chip M2, 8GB RAM, SSD da 256GB e display Liquid Retina da 13.6 pollici'),
('Dell XPS 13', 1199.99, 'LAPTOP', 'Laptop premium con processore Intel Core i7, 16GB RAM, SSD da 512GB e display InfinityEdge'),
('iPad Pro 12.9 pollici', 1099.99, 'TABLET', 'Tablet professionale con chip M2, display Liquid Retina XDR e supporto per Apple Pencil'),
('Samsung Galaxy Tab S8', 699.99, 'TABLET', 'Tablet Android con display AMOLED da 11 pollici, S Pen inclusa e processore Snapdragon'),
('AirPods Pro', 279.99, 'AURICOLARI', 'Auricolari wireless con cancellazione attiva del rumore e chip H2'),
('Sony WH-1000XM5', 399.99, 'CUFFIE', 'Cuffie over-ear con cancellazione del rumore leader del settore e 30 ore di autonomia'),
('Nintendo Switch OLED', 349.99, 'CONSOLE', 'Console portatile con display OLED da 7 pollici e dock per TV'),
('PlayStation 5', 499.99, 'CONSOLE', 'Console di gioco di nuova generazione con SSD ultra-veloce e ray tracing');

-- Inserimento commenti
INSERT INTO commento (utente_id, prodotto_id, testo, data_creazione) VALUES 
(1, 1, 'Ottimo smartphone, la fotocamera e fantastica!', CURRENT_TIMESTAMP),
(2, 1, 'Prezzo un po alto ma ne vale la pena', CURRENT_TIMESTAMP - INTERVAL '2 days'),
(3, 2, 'Preferisco Android, molto fluido', CURRENT_TIMESTAMP - INTERVAL '1 day'),
(4, 3, 'Perfetto per il lavoro, molto veloce', CURRENT_TIMESTAMP - INTERVAL '3 days'),
(5, 3, 'Il chip M2 e incredibile, consigliato!', CURRENT_TIMESTAMP - INTERVAL '1 hour'),
(6, 5, 'Ideale per il disegno digitale', CURRENT_TIMESTAMP - INTERVAL '5 hours'),
(7, 7, 'Qualita audio eccellente', CURRENT_TIMESTAMP - INTERVAL '12 hours'),
(8, 8, 'Le migliori cuffie che abbia mai avuto', CURRENT_TIMESTAMP - INTERVAL '6 hours'),
(9, 9, 'Perfetta per giocare ovunque', CURRENT_TIMESTAMP - INTERVAL '2 hours'),
(10, 10, 'Grafica pazzesca, molto contento', CURRENT_TIMESTAMP - INTERVAL '8 hours');

-- Inserimento relazioni prodotti simili
INSERT INTO prodotti_simili (prodotto_id, simile_id) VALUES 
(1, 2), (2, 1),
(3, 4), (4, 3),
(5, 6), (6, 5),
(7, 8), (8, 7),
(9, 10), (10, 9),
(1, 5), (5, 1),
(2, 6), (6, 2),
(3, 5), (5, 3);*/