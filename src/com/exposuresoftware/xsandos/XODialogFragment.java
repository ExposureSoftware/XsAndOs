package com.exposuresoftware.xsandos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;

public class XODialogFragment extends DialogFragment {
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder dialog_builder = new AlertDialog.Builder( getActivity() );
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		dialog_builder.setView( inflater.inflate( R.layout.dialog_base, null ) );
		
		return dialog_builder.create();
	}
	
	public void setTitle( String title ) {
		this.setTitle( title );
	}

}
