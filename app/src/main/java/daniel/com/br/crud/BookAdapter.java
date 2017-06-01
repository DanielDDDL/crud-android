package daniel.com.br.crud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import daniel.com.br.crud.model.Book;

/**
 * Created by Daniel D. de Lima on 01/06/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, List<Book> listBooks){
        super(context,0,listBooks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Book book = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_list_item,parent,false);
        }

        TextView txtTitlte = (TextView)convertView.findViewById(R.id.lblTitle);
        TextView txtAuthor = (TextView)convertView.findViewById(R.id.lblAuthor);
        ImageView itemIcon = (ImageView)convertView.findViewById(R.id.imageIconItem);

        txtTitlte.setText(book.getTitle());
        txtAuthor.setText(book.getAuthor());

        //choosing what image
        if (book.getGenre().equals("policial")){
            itemIcon.setImageResource(R.drawable.policial);
        }

        else if (book.getGenre().equals("fantasy")){
            itemIcon.setImageResource(R.drawable.fantasy);
        }

        else if (book.getGenre().equals("romance")){
            itemIcon.setImageResource(R.drawable.romance);
        }

        else if (book.getGenre().equals("action")) {
            itemIcon.setImageResource(R.drawable.action);

        } else {
            itemIcon.setImageResource(R.drawable.default_image);
        }
        
        return convertView;

    }
}
