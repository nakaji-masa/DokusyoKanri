package android.wings.websarva.todooutputapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;



public class OrderConfirmDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle(R.string.dialog_title);

            builder.setMessage(R.string.dialog_msg);

            builder.setPositiveButton(R.string.dialog_ok, new DialogButtonClickListener());

            builder.setNegativeButton(R.string.dialog_ng, new DialogButtonClickListener());


            AlertDialog dialog = builder.create();

            return dialog;


        }

        private class DialogButtonClickListener implements DialogInterface.OnClickListener{
            @Override
            public void onClick(DialogInterface dialog, int which){

                switch(which){
                    case DialogInterface.BUTTON_NEGATIVE:
                        Book_RegisterActivity book_registerActivity = new Book_RegisterActivity();
                        int id = book_registerActivity.idCount;
                        Intent intent = new Intent(getContext(), Book_detail.class);
                        intent.putExtra("_id", id);
                        startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_POSITIVE:
                        break;


                }
            }
        }
    }


