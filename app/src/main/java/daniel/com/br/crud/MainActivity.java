package daniel.com.br.crud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //tests porpuses
        Intent newActivityIntent = new Intent(this,UpdateOrDeleteActivity.class);
        startActivity(newActivityIntent);

    }
}
