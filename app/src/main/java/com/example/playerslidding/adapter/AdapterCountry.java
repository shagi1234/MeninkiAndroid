package com.example.playerslidding.adapter;


import static com.example.playerslidding.utils.Const.mainFragmentManager;
import static com.example.playerslidding.utils.StaticMethods.dpToPx;
import static com.example.playerslidding.utils.StaticMethods.setPadding;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playerslidding.R;
import com.example.playerslidding.data.CountryDto;
import com.example.playerslidding.databinding.FragmentCountryAndNumberBinding;
import com.example.playerslidding.fragment.FragmentCountryCode;
import com.example.playerslidding.fragment.FragmentLanguage;
import com.example.playerslidding.interfaces.CountryClickListener;

import java.util.List;

public class AdapterCountry extends RecyclerView.Adapter<AdapterCountry.ViewHolder> {
    private Context context;
    private int[] array;
    private List<CountryDto> countries;
    private int type;
    private FragmentActivity activity;

    public AdapterCountry(Context context, List<CountryDto> countries, FragmentActivity activity, int type) {
        this.context = context;
        this.countries = countries;
        this.activity = activity;
        this.type = type;
        array = new int[getItemCount()];

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_country_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.nameCountry.setText(countries.get(holder.getAdapterPosition()).getName());
        if (type != FragmentCountryCode.TYPE_LANGUAGE) {
            holder.firstLetter.setVisibility(View.VISIBLE);
            setPadding(holder.itemView, 0, 0, dpToPx(20, context), 0);
            holder.countryCode.setText("+" + countries.get(holder.getAdapterPosition()).getCode());

            if (array[position] == 0) {
                if (holder.getAdapterPosition() != 0 &&
                        countries.get(holder.getAdapterPosition() - 1)
                                .getName().toUpperCase().substring(0, 1)
                                .equals(countries.get(holder.getAdapterPosition())
                                        .getName().toUpperCase().substring(0, 1))) {
                    holder.firstLetter.setText("F");
                    holder.firstLetter.setVisibility(View.INVISIBLE);
                    array[position] = 1;

                } else {
                    holder.firstLetter.setText(countries.get(holder.getAdapterPosition()).getName().toUpperCase().substring(0, 1));
                    holder.firstLetter.setVisibility(View.VISIBLE);
                    array[position] = 2;
                }
            } else if (array[position] == 2) {
                holder.firstLetter.setText(countries.get(holder.getAdapterPosition()).getName().toUpperCase().substring(0, 1));
                holder.firstLetter.setVisibility(View.VISIBLE);
            } else if (array[position] == 1) {
                holder.firstLetter.setText("F");
                holder.firstLetter.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.firstLetter.setVisibility(View.GONE);
            setPadding(holder.itemView, dpToPx(20, context), 0, dpToPx(20, context), 0);
        }

        holder.itemView.setOnClickListener(v -> activity.runOnUiThread(() -> {
            Fragment language = mainFragmentManager.findFragmentByTag(FragmentLanguage.class.getName());
            Fragment fragmentCountryAndNumberBinding = mainFragmentManager.findFragmentByTag(FragmentCountryAndNumberBinding.class.getName());

            if (type == FragmentCountryCode.TYPE_COUNTRY_CODE && fragmentCountryAndNumberBinding instanceof CountryClickListener) {
                ((CountryClickListener) fragmentCountryAndNumberBinding).countryClick(countries.get(holder.getAdapterPosition()).getName() + "", countries.get(holder.getAdapterPosition()).getCode());
            }

            if (type == FragmentCountryCode.TYPE_LANGUAGE && language instanceof CountryClickListener) {
                ((CountryClickListener) language).countryClick(countries.get(holder.getAdapterPosition()).getName() + "", countries.get(holder.getAdapterPosition()).getCode());
            }

            activity.onBackPressed();
        }));
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView firstLetter;
        private TextView nameCountry;
        private TextView countryCode;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            firstLetter = itemView.findViewById(R.id.first_letter);
            nameCountry = itemView.findViewById(R.id.name_country);
            countryCode = itemView.findViewById(R.id.country_code);
        }

    }
}
