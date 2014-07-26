package org.abner.manager.activities.main;

public enum Program {
    CADASTRO("Cadastro"),
    GASTOS("Gastos"),
    SMS("Sms"),
    RELATORIOS("Relatórios");

    private String title;

    private Program(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static String[] getTitles() {
        String[] programs = new String[Program.values().length];
        Program[] values = Program.values();
        for (int i = 0; i < values.length; i++) {
            programs[i] = values[i].getTitle();
        }
        return programs;
    }

}
