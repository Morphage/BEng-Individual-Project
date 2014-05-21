DROP TABLE StudentExerciseRecord;
DROP TABLE ExerciseBank;
DROP TABLE History;
DROP TABLE Performance;
DROP TABLE Category;
DROP TABLE Student;

CREATE TABLE Student (
    login_name             text NOT NULL PRIMARY KEY,
    first_name             text NOT NULL,
    last_name              text NOT NULL,
	last_login             date,
	last_exercise_answered date
);

INSERT INTO Student VALUES ('ac6609', 'Alexis', 'Chantreau', '04/05/2014', '04/05/2014');
INSERT INTO Student VALUES ('jd4510', 'John', 'Doe', '04/05/2014', '04/05/2014');
INSERT INTO Student VALUES ('md1406', 'Matt', 'Damon', '04/05/2014', '04/05/2014');

CREATE TABLE Category (
    exercise_category text NOT NULL PRIMARY KEY,
    description       text,
    lecture_notes     text,
    helpful_links     text,
    visible           boolean
);

INSERT INTO Category VALUES ('Arrays', 
                             'An array is a data type that is meant to describe a collection of elements (values or variables), each selected by one or more indices that can be computed at runtime by the program.',
                             'https://cate.doc.ic.ac.uk',
                             'http://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.htmljdshjdhfsjdhfjdshjfhdsjfhdjfhjdshfjshdjfhd;http://www.tutorialspoint.com/java/java_arrays.htm',
                             true);

INSERT INTO Category VALUES ('Syntax',
                             'The syntax of the Java programming language is the set of rules defining how a Java program is written and interpreted. The syntax is mostly derived from C and C++.', 
                             'https://cate.doc.ic.ac.uk',
                             'http://docs.oracle.com/javase/tutorial/java/nutsandbolts/index.html;http://introcs.cs.princeton.edu/java/11cheatsheet/;http://tutorialspoint.com/java/java_basic_syntax.htm',
                             true);

INSERT INTO Category VALUES ('Conditionals',
                             'Conditionals',
                             'conditionals',
                             'conditionals',
                             true);

INSERT INTO Category VALUES ('Loops',
                             'loops',
                             'loops',
                             'loops',
                             true);

INSERT INTO Category VALUES ('Objects',
                             'objects',
                             'objects',
                             'objects',
                             true);

INSERT INTO Category VALUES ('Binary Trees',
                             'trees',
                             'trees',
                             'trees',
                             true);

INSERT INTO Category VALUES ('Strings',
                             'strings',
                             'strings',
                             'strings',
                             true);

CREATE TABLE Performance (
    login_name          text REFERENCES Student (login_name),
	exercise_category   text REFERENCES Category (exercise_category),
	exercises_answered  integer,
	correct_answers     integer,
	wrong_answers       integer
);

INSERT INTO Performance VALUES ('ac6609', 'Arrays', 10, 9, 1);
INSERT INTO Performance VALUES ('ac6609', 'Syntax', 20, 15, 5);
INSERT INTO Performance VALUES ('ac6609', 'Loops', 45, 20, 25);
INSERT INTO Performance VALUES ('ac6609', 'Binary Trees', 20, 7, 13);
INSERT INTO Performance VALUES ('ac6609', 'Conditionals', 50, 30, 20);
INSERT INTO Performance VALUES ('ac6609', 'Objects', 17, 8, 9);
INSERT INTO Performance VALUES ('ac6609', 'Strings', 0, 0, 0);

CREATE TABLE History (
    login_name          text REFERENCES Student (login_name),
    day                 date,
    exercises_answered  integer,
    correct_answers     integer,
    wrong_answers       integer
);

CREATE TABLE ExerciseBank (
    exercise_id       SERIAL NOT NULL PRIMARY KEY,
    exercise_category text REFERENCES Category (exercise_category),
    exercise_text     text NOT NULL,
    correct_answers   integer,
    wrong_answers     integer
);

CREATE TABLE StudentExerciseRecord (
    login_name  text REFERENCES Student (login_name),
    exercise_id integer REFERENCES ExerciseBank (exercise_id),
    answer_id   integer
);
