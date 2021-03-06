package com.velvetPearl.lottery.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.velvetPearl.lottery.R;
import com.velvetPearl.lottery.ApplicationDomain;
import com.velvetPearl.lottery.dataAccess.DataAccessEvent;
import com.velvetPearl.lottery.dataAccess.models.Lottery;
import com.velvetPearl.lottery.dataAccess.models.LotteryNumber;

import java.util.LinkedList;

/**
 * Created by Stensig on 27-Oct-16.
 */

public class NewLotteryNumDlg extends DialogFragment implements View.OnClickListener {

    // UI elements
    private Button randomBtn;
    private Button specificNumBtn;
    private EditText numberInput;
    private TextView errorLabel;

    private String ticketId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_new_lottery_num_dlg, container, false);

        initUi(root);

        return root;
    }

    private void initUi(View rootView) {
        randomBtn = (Button) rootView.findViewById(R.id.lottery_num_random_btn);
        specificNumBtn = (Button) rootView.findViewById(R.id.lottery_num_specific_btn);
        numberInput = (EditText) rootView.findViewById(R.id.lottery_num_input);
        errorLabel = (TextView) rootView.findViewById(R.id.lottery_num_error_lab);

        randomBtn.setOnClickListener(this);
        specificNumBtn.setOnClickListener(this);

        errorLabel.setText(null);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }


    @Override
    public void onClick(View v) {
        if (v == randomBtn) {

            int upperBound = ApplicationDomain.getInstance().getActiveLottery().getLotteryNumUpperBound();
            int lowerBound = ApplicationDomain.getInstance().getActiveLottery().getLotteryNumLowerBound();
            LinkedList<Integer> usedNumbers = ApplicationDomain.getInstance().getUsedLotteryNumbers();
            int randomNumber;

            do {
                randomNumber = (int) (Math.random() * (upperBound + 1));
            } while (randomNumber < lowerBound || usedNumbers.contains(randomNumber));

            storeLotteryNumber(randomNumber);
            getFragmentManager().popBackStack();
        } else if (v == specificNumBtn) {

            String input = numberInput.getText().toString();
            int userNumber;
            try {
                userNumber = Integer.parseInt(input);
            } catch (Exception e) {
                errorLabel.setText(R.string.lottery_num_error_format);
                return;
            }

            Lottery lottery = ApplicationDomain.getInstance().getActiveLottery();
            if (userNumber < lottery.getLotteryNumLowerBound() || userNumber > lottery.getLotteryNumUpperBound()) {
                errorLabel.setText(String.format(getString(R.string.lottery_num_error_range),lottery.getLotteryNumLowerBound(), lottery.getLotteryNumUpperBound()));
                return;
            }

            if (ApplicationDomain.getInstance().getUsedLotteryNumbers().contains(userNumber)){
                errorLabel.setText(R.string.lottery_num_error_unavailable);
                return;
            }

            storeLotteryNumber(userNumber);
            getFragmentManager().popBackStack();
        }
    }

    private void storeLotteryNumber(int number) {
        Lottery lottery = ApplicationDomain.getInstance().getActiveLottery();
        LotteryNumber lotteryNumber = new LotteryNumber();
        lotteryNumber.setTicketId(ticketId);
        lotteryNumber.setLotteryNumber(number);
        lotteryNumber.setPrice(lottery.getPricePerLotteryNum());

        ApplicationDomain.getInstance().getEditingTicket().getUnsavedNumbers().add(lotteryNumber);
        ApplicationDomain.getInstance().broadcastChange(DataAccessEvent.LOTTERY_NUMBER_UPDATE);
    }
}
