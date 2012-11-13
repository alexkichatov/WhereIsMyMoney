package ru.spbau.WhereIsMyMoney.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ru.spbau.WhereIsMyMoney.Transaction;
import ru.spbau.WhereIsMyMoney.storage.TransactionLogSource;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Shows list of transactions from specified card.
 */
public class TransactionsListActivity extends Activity {
	private static final String FORMAT = "yyyy.MM.dd HH:mm:ss";
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat(FORMAT);
	
    static final String ID_PARAM = "id";
    final String OK = "Ok";
    final String TRANSACTION = "Transaction description";
    TransactionLogSource db;

    private void createTransactionsDialog(String title, String message) {
        AlertDialog.Builder ad = new AlertDialog.Builder(TransactionsListActivity.this);
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setCancelable(true);
        ad.setPositiveButton(OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            }
        });
        ad.show();
    }
    
    private String shortDescription(Transaction trans) {
    	StringBuilder sb = new StringBuilder();
    	sb.append(FORMATTER.format(trans.getDate()));
    	if (trans.getDelta() != null) {
    		sb.append(", delta: ").append(trans.getDelta());
    	} else {
    		sb.append(", balance: ").append(trans.getBalance());
    	}
    	return sb.toString();
    }

    private void createListView(final String card) {
        ListView listView = (ListView) findViewById(ru.spbau.WhereIsMyMoney.R.id.transactions);

        final List<Transaction> transactions = db.getTransactionsPerCard(card);
        final String[] values = new String[transactions.size()];
        final String[] messages = new String[transactions.size()];
        for (int i = 0; i < transactions.size(); ++i) {
        	Transaction trans = transactions.get(i);
            values[i] = shortDescription(trans);
            messages[i] = trans.toString();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                createTransactionsDialog(TRANSACTION, messages[position]);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String message = intent.getStringExtra(ID_PARAM);
        setContentView(ru.spbau.WhereIsMyMoney.R.layout.transactions);
        db = new TransactionLogSource(getApplicationContext());
        db.open();
        TextView card = (TextView) findViewById(ru.spbau.WhereIsMyMoney.R.id.card_id);
        card.setText(message);
        createListView(message);
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }
}