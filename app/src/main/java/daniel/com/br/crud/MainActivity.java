package daniel.com.br.crud;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ListView listBooks;

    //test
    private ArrayAdapter<String>testAdapter;
    private static final String[] FRUITS = new String[] { "Apple", "Avocado", "Banana",
            "Blueberry", "Coconut", "Durian", "Guava", "Kiwifruit",
            "Jackfruit", "Mango", "Olive", "Pear", "Sugar-apple" };
    private Context testContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



//        this.testContext = this;
//        listBooks = (ListView)findViewById(R.id.listBooks);
//        testAdapter = new ArrayAdapter<String>(this,R.layout.simple_list_item,FRUITS);
//        listBooks.setAdapter(testAdapter);
//        //tests porpuses
        Intent newActivityIntent = new Intent(this,NewBookActivity.class);
        startActivity(newActivityIntent);

    }
}
