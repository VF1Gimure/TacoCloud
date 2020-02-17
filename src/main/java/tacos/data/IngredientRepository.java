package tacos.data;
import org.springframework.data.repository.CrudRepository;
import tacos.model.Ingredient;

/*CrudRepository declares about a dozen methods for CRUD
 * (create, read, update, delete) operations.
 * Notice that it’s parameterized, with the first parameter being
 * the entity type the repository is to persist, and the second parameter
 * being the type of the entity ID property.
 *
 */
public interface IngredientRepository extends CrudRepository<Ingredient, String> {

}
