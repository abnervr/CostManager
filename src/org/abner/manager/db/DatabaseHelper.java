package org.abner.manager.db;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.abner.manager.model.AbstractModel;
import org.abner.manager.model.Model;
import org.abner.manager.model.empresa.Empresa;
import org.abner.manager.model.empresa.Estabelecimento;
import org.abner.manager.model.empresa.Telefone;
import org.abner.manager.model.empresa.Tipo;
import org.abner.manager.model.movimento.Movimento;
import org.abner.manager.model.movimento.MovimentoItem;
import org.abner.manager.model.posicao.Cidade;
import org.abner.manager.model.posicao.Endereco;
import org.abner.manager.model.posicao.Estado;
import org.abner.manager.model.posicao.Pais;
import org.abner.manager.model.produto.Classe;
import org.abner.manager.model.produto.Produto;
import org.abner.manager.model.sms.Sms;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CostManager";
    private static final int DATABASE_VERSION = 1;

    private final Class<?>[] models = {
                    Empresa.class, Estabelecimento.class, Telefone.class, Tipo.class,

                    Movimento.class, MovimentoItem.class,

                    Cidade.class, Endereco.class, Estado.class, Pais.class,

                    Classe.class, Produto.class,

                    Sms.class};

    private final Map<Class<?>, List<Field>> fields = new HashMap<Class<?>, List<Field>>();

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            for (Class<?> clazz : models) {
                if (Model.class.isAssignableFrom(clazz)) {
                    String ddl = getDDL((Class<? extends Model>) clazz);
                    Log.i("DatabaseHelper", ddl);
                    db.execSQL(ddl);
                } else {
                    Log.e("DatabaseHelper", clazz.getName());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("DatabaseHelper",
                        "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        for (Class<?> clazz : models) {
            if (Model.class.isAssignableFrom(clazz)) {
                db.execSQL("DROP TABLE IF EXISTS " + getTableName((Class<? extends Model>) clazz));
            } else {
                Log.e("DatabaseHelper", clazz.getName());
            }
        }
        onCreate(db);
    }

    /**Monta o create table com base nos fields da classe.<br>
     *
     * <ul>
     * <li> Fields do tipo <code>String</code> e <code>Date</code> serão convertidos para campos do tipo text
     * <li> Fields do tipo <code>Integer</code> e <code>Long</code> serão convertidos para campos do tipo integer
     * <li> Fields que herdem a classe <code>AbstractModel</code> serão convertidos para campos do tipo integer com o sufixo Id
     * </ul>
     * @param model
     * @return DDL do model
     */
    private String getDDL(Class<? extends Model> model) {
        StringBuilder builder = new StringBuilder();
        builder.append("create table ");
        builder.append(getTableName(model));
        builder.append("(");
        boolean hasData = false;

        for (Field field : getFields(model)) {
            String type = null;
            String suffix = null;

            if (field.getType().equals(String.class) ||
                            field.getType().equals(BigDecimal.class) ||
                            field.getType().isEnum()) {
                type = "text";

            } else if (field.getType().equals(Integer.class) ||
                            field.getType().equals(int.class) ||
                            field.getType().equals(Long.class) ||
                            field.getType().equals(long.class) ||
                            field.getType().equals(Date.class) ||
                            field.getType().equals(Boolean.class) ||
                            field.getType().equals(boolean.class)) {
                type = "integer";

                if (field.getName().equals("id")) {
                    type += " primary key";
                }

            } else if (AbstractModel.class.isAssignableFrom(field.getType())) {
                type = "integer";
                suffix = "Id";
            }

            if (type != null) {

                if (hasData) {
                    builder.append(',');
                } else {
                    hasData = true;
                }

                builder.append(field.getName());

                if (suffix != null) {
                    builder.append(suffix);
                }

                builder.append(' ');
                builder.append(type);

            }
        }

        builder.append(");");
        return builder.toString();
    }

    /**
     * Gera o nome da tabela através do nome da classe.<br>
     * O nome gerado será separado por um underline _<br>
     * 
     * Exemplo: <br>
     * <ul>
     * <li>NomeDaClasse => nome_da_classe
     * <li>Pessoa => pessoa
     * <li>Vaga => vaga
     * <li>PessoaVaga => pessoa_vaga
     * </ul>
     * 
     * @param clazz
     * @return
     */
    public String getTableName(Class<? extends Model> clazz) {
        String simpleName = clazz.getSimpleName();
        String name = "";

        for (char c : simpleName.toCharArray()) {

            if (name.isEmpty()) {
                name += Character.toLowerCase(c);

            } else if (Character.isUpperCase(c)) {
                name += "_" + Character.toLowerCase(c);

            } else {
                name += c;
            }
        }

        return name;
    }

    /**
     *  Todos os Fields que forem encontrados na hierarquia da classe.<br>
     *  Fields com o modificador <code>transient</code> e  <code>static</code> são ignorados.
     *  
     * @param model
     * @return fields que serão persistidos
     */
    public List<Field> getFields(Class<? extends Model> model) {
        return getFields(model, true);
    }

    public List<Field> getFields(Class<?> model, boolean ignoreTransient) {

        if (Long.class.isAssignableFrom(model)) {
            return null;
        } else if (fields.containsKey(model)) {
            return fields.get(model);
        }

        List<Field> fields = new ArrayList<Field>();
        Class<?> clazz = model;

        while (clazz != null) {

            for (Field field : clazz.getDeclaredFields()) {

                if ((!ignoreTransient || (field.getModifiers() & Modifier.TRANSIENT) == 0) && (field.getModifiers() & Modifier.STATIC) == 0) {
                    fields.add(field);
                }
            }

            clazz = clazz.getSuperclass();
        }

        this.fields.put(model, fields);
        return fields;
    }

}
