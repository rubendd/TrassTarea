package com.rdd.trasstarea.activities.listactivity.dialogs;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

/**
 * De momento no se usa :)
 */
public class DeleteDialog extends DialogFragment {

    public interface DeleteDialogListener{
        void onPositiveBtn();
        void  onNegativeBtn();
    }

    public void setDeleteDialogListener(DeleteDialogListener listener) {
        mListener = listener;
    }

    private DeleteDialogListener mListener;

    public boolean showDelete(Context activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Eliminar")
                .setMessage("¿Estás seguro de que deseas eliminar?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    if (mListener != null) {
                        mListener.onPositiveBtn();
                    }
                    // Dismiss the dialog if needed
                    dialog.dismiss();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Dismiss the dialog if needed
                    dialog.dismiss();
                });

        AlertDialog dialog = builder.create();
        dialog.show();

        // Indica que el diálogo está en pantalla
        return true;
    }



}
