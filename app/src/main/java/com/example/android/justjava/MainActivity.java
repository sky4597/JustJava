/**
 * Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 * <p>
 * package com.example.android.justjava;
 */

package com.example.android.justjava;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

import static android.R.attr.order;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 2;
    String b, c;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.imp);
        boolean isLang = Locale.getDefault().getLanguage().equals("fr");
        Log.i("MainActivity","The value of \'isLang\' is "+isLang);



        if(isLang)
        mediaPlayer.start();



    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);

        Log.i("MainActivity", "Has whipped cream: " + whippedCreamCheckBox.isChecked());
        Log.i("MainActivity", "Has chocolate: " + chocolateCheckBox.isChecked());

        EditText text = (EditText) findViewById(R.id.name_field);
        String name = text.getText().toString();

        Log.v("MainActivity", "Name: " + name);

        if (whippedCreamCheckBox.isChecked())
            b = getString(R.string.yes);
        else
            b = getString(R.string.no);

        if (chocolateCheckBox.isChecked())
            c = getString(R.string.yes);
        else
            c = getString(R.string.no);


        int price = calculatePrice(whippedCreamCheckBox.isChecked(), chocolateCheckBox.isChecked());
        int mainActivity = Log.v("MainActivity", "The price is " + price);
        String priceMessage = createOrderSummary(name, price, b, c);
        displayMessage(priceMessage);


        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject)+" "+ name);
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(message);
    }

    /**
     * This method is called when the "+" button is clicked.
     */
    public void increment(View view) {
        if (quantity == 100) {
            // Show an error message as a toast
            Toast.makeText(this, "You cannot have more than 100 coffees", Toast.LENGTH_SHORT).show();
            // Exit this method early because there's nothing left to do
            return;
        }
        quantity = quantity + 1;
        display(quantity);
    }

    /**
     * This method is called when the "+" button is clicked.
     */
    public void decrement(View view) {
        if (quantity == 1) {
            // Show an error message as a toast
            Toast.makeText(this, "You cannot have less than 1 coffee", Toast.LENGTH_SHORT).show();
            // Exit this method early because there's nothing left to do
            return;
        }
        quantity = quantity - 1;
        display(quantity);
    }

    /*
*
 *   Create summary of the order
  *  @param price of the order
   * @param name of the customer
    *@param b is a string saying "Yes!" or "No!" storing user's choice of whipped cream.
     *@return text summary
*/
    private String createOrderSummary(String name, int price, String b, String c)

    {
        String priceMessage = getString(R.string.order_summary_name) +" "+name;
        Log.i("MainActivity","My name is "+getString(R.string.order_summary_name));
        priceMessage += "\n"+getString(R.string.order_summary_whipped_cream)+" " + b;
        priceMessage += "\n"+getString(R.string.order_summary_chocolate)+" " + c;
        priceMessage += "\n"+getString(R.string.quant)+" " + quantity;
        priceMessage += "\n"+getString(R.string.order_summary_price)+" "+ displayPrice(price);
        priceMessage += "\n"+getString(R.string.thank_you);
        return priceMessage;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /*
    Calculates the price of order
    @param hasWhippedCream is whether or not user wants whipped cream topping
    @param hasChocolate is whether or not user wants chocolate topping
    @return total price
     */

    public int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        //Price of 1 cup of coffee
        int basePrice = 5;

        //Add 1$ if the user wants whipped cream
        if (hasWhippedCream)
            basePrice += 1;

        //Add 2$ if user wants chocolate
        if (hasChocolate)
            basePrice += 2;

        //Calculate total order price by multiplying with quantity
        return quantity * basePrice;
    }

    /**
     * This method displays the given price on the screen.
     */
    private String displayPrice(int number) {
        String price = NumberFormat.getCurrencyInstance().format(number);
        price=price.replace(',','.');

        return price;
    }
}