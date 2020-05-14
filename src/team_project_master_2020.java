/**
 * test comment
 */

public class team_project_master_2020 {



    private String test;
    private int age;

    team_project_master_2020(String name, int age){
        this.test = name;
        this.age = age;
    }

    String changeName(String newName){
        this.test = newName;
        return test;
    }

    void print() {
        System.out.println("Name: " + test + "\n"
                            + "Age: " + age);
    }

    public static void main(String[] args) {
        team_project_master_2020 t1 = new team_project_master_2020("Example", 2);
        t1.print();
    }
}
