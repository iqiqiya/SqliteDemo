package iqiqiya.lanlana.sqlitedemo;

/**
 * Author: iqiqiya
 * Date: 2019/10/16
 * Time: 16:19
 * Blog: blog.77sec.cn
 * Github: github.com/iqiqiya
 */
public class Student {
        //私有属性
        private int id;
        private String name;
        private int age;
        private String gender;
        //无参构造
        public Student(){

        }

        public Student(String name, int age, String gender) {
            this.name = name;
            this.age = age;
            this.gender = gender;
        }

        //有参构造
        public Student(int id, String name, int age, String gender) {
            super();
            this.id = id;
            this.name = name;
            this.age = age;
            this.gender = gender;
        }
        //创建的setter和getter方法
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getAge() {
            return age;
        }
        public void setAge(int age) {
            this.age = age;
        }
        public String getGender() {
            return gender;
        }
        public void setGender(String gender) {
            this.gender = gender;
        }
    }
