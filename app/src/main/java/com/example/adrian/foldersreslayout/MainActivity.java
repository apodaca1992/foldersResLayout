package com.example.adrian.foldersreslayout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_listado_prueba, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_listado_prueba:
                Intent iAgregarNuevaAfiliacion = new Intent(MainActivity.this, ListadoPrueba.class);
                startActivity(iAgregarNuevaAfiliacion);
                //finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
