-- ═══════════════════════════════════════════════════════════════
--  LibraryMS — Database Schema  (matches Spring Boot backend v2)
-- ═══════════════════════════════════════════════════════════════
CREATE DATABASE IF NOT EXISTS libraryms CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE libraryms;

-- ── member ──────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS member (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    name              VARCHAR(120)  NOT NULL,
    email             VARCHAR(120)  NOT NULL UNIQUE,
    password          VARCHAR(255)  NOT NULL,
    phone             VARCHAR(30),
    address           VARCHAR(255),
    role              ENUM('ADMIN','LIBRARIAN','USER') NOT NULL DEFAULT 'USER',
    active            TINYINT(1)    NOT NULL DEFAULT 1,
    member_id_number  VARCHAR(50),
    nic_front_path    VARCHAR(500),
    nic_back_path     VARCHAR(500),
    id_verified       TINYINT(1)    NOT NULL DEFAULT 0,   -- ADDED: was missing from original SQL
    created_at        DATETIME      DEFAULT CURRENT_TIMESTAMP,
    updated_at        DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ── book ────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS book (
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    title              VARCHAR(200)  NOT NULL,
    author             VARCHAR(150)  NOT NULL,
    isbn               VARCHAR(60)   NOT NULL UNIQUE,
    category           VARCHAR(80),
    description        TEXT,
    quantity           INT           NOT NULL DEFAULT 1,
    available_quantity INT           NOT NULL DEFAULT 1,
    published_year     INT,
    cover_image_url    VARCHAR(500),
    barcode_value      VARCHAR(100)  UNIQUE,
    created_at         DATETIME      DEFAULT CURRENT_TIMESTAMP,
    updated_at         DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ── barcode ─────────────────────────────────────────────────────
-- ADDED: BarCode.java entity maps to this table, was completely missing
CREATE TABLE IF NOT EXISTS barcode (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    code      VARCHAR(100) NOT NULL UNIQUE,
    book_id   BIGINT       NOT NULL UNIQUE,
    qr_data   TEXT,
    file_path VARCHAR(500),
    FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE
);

-- ── borrow_record ───────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS borrow_record (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id   BIGINT  NOT NULL,
    book_id     BIGINT  NOT NULL,
    borrow_date DATE    NOT NULL,
    due_date    DATE    NOT NULL,
    return_date DATE,
    status      ENUM('BORROWED','RETURNED','OVERDUE','LOST') NOT NULL DEFAULT 'BORROWED',
    fine_amount DOUBLE  NOT NULL DEFAULT 0.0,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (book_id)   REFERENCES book(id)
);

-- ── Seed: default admin & librarian ─────────────────────────────
-- Password for both accounts: Admin@123
INSERT IGNORE INTO member (name, email, password, role, active, member_id_number, id_verified) VALUES
('Admin User',     'admin@library.com',     '$2a$12$4yZFMnCxV.P5kSOIFNemGepDlN0rJWHbw6y82LBbwTcq2MYyOJhIu', 'ADMIN',     1, 'LIB-ADMIN-001', 1),
('Librarian One',  'librarian@library.com', '$2a$12$4yZFMnCxV.P5kSOIFNemGepDlN0rJWHbw6y82LBbwTcq2MYyOJhIu', 'LIBRARIAN', 1, 'LIB-LIB-001',   1);

-- ── Seed: books ─────────────────────────────────────────────────
INSERT IGNORE INTO book (title,author,isbn,category,quantity,available_quantity,published_year,barcode_value,description) VALUES
('Natural Resources','Robin Kerrod','1-85435-628-3','General',15,14,1997,'BC-000001','Explores earths natural resources including minerals and renewable energy.'),
('Encyclopedia Americana','Grolier','0-7172-0119-8','Encyclopedia',20,18,1988,'BC-000002','Comprehensive American encyclopedia covering arts, science and history.'),
('Algebra 1','Carolyn Bradshaw','0-13-125087-6','Math',35,30,2004,'BC-000003','Foundational algebra textbook covering equations and functions.'),
('Science in our World','Brian Knapp','0-13-050841-1','Science',25,23,1996,'BC-000004','Comprehensive science series exploring physics chemistry and biology.'),
('Integrated Science Textbook','Merde C. Tan','971-570-124-8','Science',15,14,2009,'BC-000005','Philippine science curriculum textbook for secondary education.'),
('Algebra 2','Glencoe McGraw Hill','978-0-07-873830-2','Math',15,15,2008,'BC-000006','Advanced algebra covering polynomials matrices and complex numbers.'),
('Wiki at Panitikan','Lorenza P. Avera','971-07-1574-7','Filipiniana',28,27,2000,'BC-000007','Filipino literature anthology featuring classic and modern works.'),
('English Expressways','Virginia Bermudez','978-971-0315-33-8','English',23,22,2007,'BC-000008','English language skills textbook for secondary students.'),
('Asya Pag-usbong Ng Kabihasnan','Ricardo T. Jose','971-07-2324-3','Filipiniana',21,21,2008,'BC-000009','Asian civilizations textbook covering history and culture.'),
('Beloved a Novel','Toni Morrison','0-394-53597-9','References',13,12,1987,'BC-000010','Pulitzer Prize winning novel about slavery and its aftermath.'),
('Silver Burdett English','Judy Brim','0-382-03575-5','English',12,11,1985,'BC-000011','Classic English language arts textbook series.'),
('Introduction to Information Systems','Cristine Redoblo','123-132-001','Science',10,9,2013,'BC-000012','Comprehensive guide to computer information systems.'),
('Harry Potter and the Philosophers Stone','J.K. Rowling','978-0-7475-3269-9','Fiction',10,9,1997,'BC-000013','First book in the beloved Harry Potter fantasy series.'),
('To Kill a Mockingbird','Harper Lee','978-0-06-112008-4','Fiction',8,7,1960,'BC-000014','Classic American novel about racial injustice in the Deep South.'),
('The Great Gatsby','F. Scott Fitzgerald','978-0-7432-7356-5','Fiction',12,11,1925,'BC-000015','Jazz Age novel exploring themes of wealth love and the American Dream.'),
('1984','George Orwell','978-0-451-52493-5','Fiction',9,8,1949,'BC-000016','Dystopian masterpiece about totalitarianism and surveillance.'),
('Calculus Early Transcendentals','James Stewart','978-0-538-49790-9','Math',20,18,2012,'BC-000017','Comprehensive calculus textbook used in universities worldwide.'),
('Chemistry: The Central Science','Brown LeMay','978-0-13-293986-1','Science',14,13,2014,'BC-000018','University-level general chemistry textbook.'),
('World History: Patterns of Interaction','McDougal Littell','978-0-618-18988-0','General',18,17,2009,'BC-000019','Comprehensive world history textbook for secondary education.'),
('Philippine History','Teodoro Agoncillo','971-272-008-X','Filipiniana',22,21,2001,'BC-000020','Definitive history of the Philippine nation from pre-colonial era.'),
('Noli Me Tangere','Jose Rizal','971-503-001-0','Filipiniana',30,28,1887,'BC-000021','Rizals revolutionary novel that ignited Philippine nationalism.'),
('El Filibusterismo','Jose Rizal','971-503-002-1','Filipiniana',25,24,1891,'BC-000022','Rizals sequel exploring corruption and revolution in colonial Philippines.'),
('Biology: Unity and Diversity','Cecie Starr','978-1-111-42569-7','Science',16,15,2013,'BC-000023','Comprehensive biology textbook for college students.'),
('Physics for Scientists','Serway Jewett','978-1-285-07101-7','Science',12,11,2014,'BC-000024','University physics textbook with calculus-based approach.'),
('Introduction to Psychology','James Kalat','978-1-285-87064-5','General',10,9,2014,'BC-000025','Comprehensive introduction to psychological science and research.'),
('English Grammar in Use','Raymond Murphy','978-0-521-18906-4','English',25,24,1985,'BC-000026','The worlds best-selling grammar book for intermediate learners.'),
('Oxford English Dictionary','Oxford Press','978-0-19-861186-8','References',5,5,2010,'BC-000027','Comprehensive historical dictionary of the English language.'),
('Atlas of the World','National Geographic','978-1-4262-0634-4','References',8,8,2011,'BC-000028','Full-color world atlas with detailed maps and geographic data.'),
('The Art of War','Sun Tzu','978-1-59030-225-0','General',15,14,500,'BC-000029','Ancient Chinese military treatise on strategy and tactics.'),
('Pride and Prejudice','Jane Austen','978-0-14-143951-8','Fiction',11,10,1813,'BC-000030','Beloved romantic novel exploring class marriage and morality.'),
('Sapiens: A Brief History','Yuval Noah Harari','978-0-06-231609-7','General',7,6,2011,'BC-000031','Sweeping history of humankind from Stone Age to present.'),
('The Alchemist','Paulo Coelho','978-0-06-112241-5','Fiction',9,8,1988,'BC-000032','Philosophical novel about following ones dreams and destiny.'),
('Fundamentals of Accounting','Valix Peralta','971-931-002-1','References',20,19,2008,'BC-000033','Comprehensive accounting textbook for business students.'),
('Basic Economics','Thomas Sowell','978-0-465-08145-5','General',11,10,2014,'BC-000034','Clear introduction to economic principles without technical jargon.'),
('Microsoft Office 365 Guide','Joan Lambert','978-1-5093-0729-8','Science',8,7,2019,'BC-000035','Complete guide to Microsoft Office 365 applications.'),
('Literature Readers Choice','Glencoe','0-02-817934-y','References',20,18,2001,'BC-000036','Anthology of classic and contemporary literary works.'),
('Lexicon Universal Encyclopedia','Lexicon','0-7172-2043-6','Encyclopedia',10,9,1993,'BC-000037','Universal encyclopedia with in-depth articles on world knowledge.'),
('Science and Invention Encyclopedia','Clarke Donald','0-87475-450-z','Encyclopedia',16,16,1992,'BC-000038','Illustrated encyclopedia of scientific discoveries.'),
('The Corporate Warriors','Douglas Ramsey','0-395-35487-1','General',8,7,1987,'BC-000039','Inside look at corporate America and business leadership.'),
('El Mundo Filipino','Various Authors','971-503-010-0','Filipiniana',12,11,1995,'BC-000040','Collection of Filipino short stories and essays.');
