package com.singtel.findcard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


class FindCardAdapter extends RecyclerView.Adapter<FindCardAdapter.FindCardViewHolder> {

    private List<Integer> cardNumberList;
    private OnCardClickedListener listener;
    private List<Integer> validSelectionList;

    public FindCardAdapter(List<Integer> cardNumberList, List<Integer> validSelectionList, OnCardClickedListener listener) {
        this.cardNumberList = cardNumberList;
        this.validSelectionList = validSelectionList;
        this.listener = listener;
    }
    @NonNull
    @Override
    public FindCardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_card, viewGroup, false);
        return new FindCardViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull final FindCardViewHolder viewHolder, final int position) {
        final Integer cardNumber = cardNumberList.get(position);
        if (validSelectionList != null && validSelectionList.contains(position)) {
            viewHolder.txtAssignedNumber.setText(String.valueOf(cardNumber));
            viewHolder.layoutCard.setEnabled(false);
        } else {
            viewHolder.txtAssignedNumber.setText("?");
            viewHolder.layoutCard.setEnabled(true);
        }

        viewHolder.layoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //viewHolder.txtAssignedNumber.setText(String.valueOf(cardNumber));
                viewHolder.layoutCard.setEnabled(false);
                startCardAnimation(viewHolder.layoutCard, viewHolder.txtAssignedNumber, cardNumber, position);
            }
        });

    }
    @Override
    public int getItemCount() {
        return cardNumberList != null ? cardNumberList.size() : 0;
    }

    static class FindCardViewHolder extends RecyclerView.ViewHolder {
        TextView txtAssignedNumber;
        CardView layoutCard;

         FindCardViewHolder(View itemView) {
            super(itemView);
            layoutCard = itemView.findViewById(R.id.layout_card);
            txtAssignedNumber = itemView.findViewById(R.id.txt_assigned_number);
        }
    }

    public void refreshValidSelection(List<Integer> validSelectionList) {
        this.validSelectionList = validSelectionList;
        notifyDataSetChanged();
    }

    public interface OnCardClickedListener {
        void onCardClicked(Card card);
    }

    private void startCardAnimation(final View cardView, final TextView textView, final int cardNumber, final int position) {
        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(cardView, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(cardView, "scaleX", 0f, 1f);
        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                textView.setText(String.valueOf(cardNumber));
                oa2.start();
            }
        });

        oa2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animations) {
                super.onAnimationEnd(animations);
                if (listener != null) {
                    Card card = new Card();
                    card.setIndex(position);
                    card.setCardValue(cardNumber);
                    listener.onCardClicked(card);
                }
            }
        });
        oa1.start();
    }

}
