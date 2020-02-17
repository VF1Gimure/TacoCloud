package tacos.data;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tacos.model.Taco;
import tacos.model.Ingredient;

@Repository
public class JdbcTacoRepository implements TacoRepository {

    private JdbcTemplate jdbc;

    public JdbcTacoRepository (JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }

    @Override
    public Taco save(Taco taco) {
        long tacoId = saveTacoInfo(taco);
        taco.setId(tacoId);

        for (Ingredient ingredient : taco.getIngredients()){
            saveIngredientToTaco(ingredient, tacoId);
        }

        return taco;
    }

    /*
    * When you insert a row into Taco, you need to know the ID generated
    * by the data-base so that you can reference it in each of the ingredients.
    * The update() method, used when saving ingredient data,
    * doesn’t help you get at the generated ID, so you need a different update() method here.
    * The update() method you need accepts a PreparedStatementCreator and a Key-Holder.
    * It’s the KeyHolder that will provide the generated taco ID
    * */



    // JPA
    private long saveTacoInfo(Taco taco){
        taco.setCreatedAt(new Date());

        //creating a PreparedStatementCreator is non-trivial.
        //Start by creating a PreparedStatementCreatorFactory,
        PreparedStatementCreatorFactory pscf =
                new PreparedStatementCreatorFactory(
                        "insert into Taco (name, createdAt) values (?, ? ) ", // giving it the SQL you want to execute
                        Types.VARCHAR, Types.TIMESTAMP); //the types of each query parameter.

        pscf.setReturnGeneratedKeys(true);


        PreparedStatementCreator psc =
                pscf.newPreparedStatementCreator( //Then call newPreparedStatementCreator() on that factory
                                Arrays.asList( //passing in the values needed in the query parameters
                                    taco.getName(),
                                    new Timestamp(taco.getCreatedAt().getTime())));



        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(psc, keyHolder);
        //It’s the KeyHolder that will provide the generated taco ID
        return keyHolder.getKey().longValue();
    }

    private void saveIngredientToTaco(Ingredient ingredient, long tacoId){
        jdbc.update(
                "insert into Taco_Ingredients (taco, ingredient)" +
                        "values ( ?, ? )",
                tacoId, ingredient.getId()
        );
    }
}
