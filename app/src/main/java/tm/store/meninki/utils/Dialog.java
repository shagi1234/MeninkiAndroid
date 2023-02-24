package tm.store.meninki.utils;

import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

import tm.store.meninki.R;

public class Dialog {
    public TextView yesBtn;
    public TextView title;
    public android.app.Dialog dialog;

    public void showDialog(Context context) {
        dialog = new android.app.Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_perimission_dialog);
        dialog.setCanceledOnTouchOutside(true);
        yesBtn = dialog.findViewById(R.id.yes_btn);
        TextView noBtn = dialog.findViewById(R.id.no_btn);
        title = dialog.findViewById(R.id.question);

        noBtn.setOnClickListener(v -> dialog.dismiss());


        setBackgroundDrawable(context, dialog.findViewById(R.id.background_dialog), R.color.grey, 0, 4, false, 0);

        setBackgroundDrawable(context, dialog.findViewById(R.id.question), R.color.white, 0, 4, false, 0);

        setBackgroundDrawable(context, yesBtn, R.color.accent, 0, 4, false, 0);
        setBackgroundDrawable(context, noBtn, R.color.alert, 0, 4, false, 0);


        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }
}
