package com.mymvvmsample.screens.base.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import com.mymvvmsample.R;
import com.mymvvmsample.screens.app_abstracts.BaseDialogFragment;
import org.jetbrains.annotations.NotNull;

public class ConfirmationDialog extends BaseDialogFragment {

    public static final String TAG = "ConfirmationDialog";

    @BindView(R.id.titleText)
    TextView title;
    @BindView(R.id.title_desc)
    TextView titleDesc;

    private String titleText, msg;
    private ConfirmationDialogListener listener;

    public static ConfirmationDialog getInstance(Bundle args) {
        ConfirmationDialog dialog = new ConfirmationDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if(args != null){
            titleText = args.getString("TITLE");
            msg = args.getString("MESSAGE");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_dialog, container, false);
    }

    public void setListener(ConfirmationDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUnbinder(view);

        if(!TextUtils.isEmpty(titleText)) {
            title.setText(titleText);
        }
        if(!TextUtils.isEmpty(msg)) {
            titleDesc.setText(msg);
        }

    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(false);
        return super.onCreateDialog(savedInstanceState);

    }

    @OnClick({R.id.yes, R.id.no})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yes:
                if (listener != null) {
                    listener.onOkClicked();
                }
                dismiss();
                break;

            case R.id.no:
                dismiss();
                break;
        }
    }

    public interface ConfirmationDialogListener {
        void onOkClicked();
    }
}
