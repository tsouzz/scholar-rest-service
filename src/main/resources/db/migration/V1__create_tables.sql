CREATE TABLE schools (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         name VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE teachers (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          name VARCHAR(150) NOT NULL,
                          email VARCHAR(150) NOT NULL UNIQUE,
                          password_hash VARCHAR(255) NOT NULL,
                          school_id UUID NOT NULL,
                          CONSTRAINT fk_teacher_school FOREIGN KEY (school_id) REFERENCES schools(id)
);

CREATE TABLE class_groups (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              number INT NOT NULL,
                              module VARCHAR(25) NOT NULL,
                              teacher_id UUID NOT NULL,
                              CONSTRAINT uq_class_group UNIQUE (module, number, teacher_id),
                              CONSTRAINT fk_class_group_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE CASCADE
);

CREATE TABLE students (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          name VARCHAR(150) NOT NULL,
                          class_group_id UUID NOT NULL,
                          CONSTRAINT fk_student_class_group FOREIGN KEY (class_group_id) REFERENCES class_groups(id) ON DELETE CASCADE
);

CREATE TABLE activities (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            student_id UUID NOT NULL,
                            activity_type VARCHAR(20) NOT NULL,
                            grade DECIMAL(5,2) NOT NULL,
                            registration_date DATE NOT NULL DEFAULT CURRENT_DATE,
                            CONSTRAINT uq_activity_student_type UNIQUE (student_id, activity_type),
                            CONSTRAINT chk_grade CHECK (grade >= 0 AND grade <= 10),
                            CONSTRAINT fk_activity_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);