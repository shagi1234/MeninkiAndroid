package tm.store.meninki.adapter;

import static tm.store.meninki.utils.Option.CHARACTER_IMAGE;
import static tm.store.meninki.utils.Option.CHARACTER_TEXT;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import tm.store.meninki.R;
import tm.store.meninki.data.CharactersDto;
import tm.store.meninki.data.SelectedMedia;
import tm.store.meninki.databinding.ItemPersonalCharactersBinding;
import tm.store.meninki.utils.Dialog;

public class AdapterPersonalCharacters extends RecyclerView.Adapter<AdapterPersonalCharacters.CharactersHolder> {
    private CharactersDto character;
    private Context context;
    private String prodId;

    public AdapterPersonalCharacters(Context context, CharactersDto characters, String productId) {
        this.context = context;
        this.character = characters;
        this.prodId = productId;
    }

    @NonNull
    @Override
    public CharactersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemPersonalCharactersBinding popularAudios = ItemPersonalCharactersBinding.inflate(layoutInflater, parent, false);
        return new AdapterPersonalCharacters.CharactersHolder(popularAudios);
    }

    @Override
    public void onBindViewHolder(@NonNull CharactersHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        if (character.getOptionTitles() == null || character.getOptions() == null || character.getOptionTypes() == null) {
            return 0;
        }
        return character.getOptions().size();
    }

    private void removeAt(int position) {
        try {
            character.getOptions().remove(position);
            character.getOptionTitles().remove(position);
            character.getOptionTypes().remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, SelectedMedia.getOptionImageList().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(int position) {
        notifyItemInserted(position);
    }

    public class CharactersHolder extends RecyclerView.ViewHolder {
        private final ItemPersonalCharactersBinding b;

        public CharactersHolder(@NonNull ItemPersonalCharactersBinding itemView) {
            super(itemView.getRoot());
            this.b = itemView;
        }

        public void bind() {

            switch (character.getOptionTypes().get(getAdapterPosition())) {
                case CHARACTER_TEXT:
                    setRecyclerText();
                    break;
                case CHARACTER_IMAGE:
                    setRecyclerImage();
                    break;

            }
            b.nameCharacters.setText(character.getOptionTitles().get(getAdapterPosition()));

            b.clearCharacter.setOnClickListener(v -> removeAt(getAdapterPosition()));

            b.edtName.setOnClickListener(v -> {
                //change title
                Dialog dialog = new Dialog();
                dialog.showDialog(context);
                dialog.yesBtn.setOnClickListener(v1 -> {
                    if (dialog.title.getText().toString().trim().length() == 0) {
                        Toast.makeText(context, context.getResources().getString(R.string.your_text_is_empty_please_write_something), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    b.nameCharacters.setText(dialog.title.getText().toString().trim());
                    dialog.dialog.dismiss();
                });
            });

        }

        private void setRecyclerImage() {
            AdapterCharImage adapter = new AdapterCharImage(context, AdapterCharPick.ADDABLE, prodId, getAdapterPosition());
            b.recItem.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            b.recItem.setAdapter(adapter);

            adapter.setOptions(character.getOptions().get(getAdapterPosition()));
        }

        private void setRecyclerText() {
            AdapterCharPick adapter = new AdapterCharPick(context, AdapterCharPick.ADDABLE, prodId, getAdapterPosition());
            b.recItem.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            b.recItem.setAdapter(adapter);

            adapter.setTexts(character.getOptions().get(getAdapterPosition()));
        }
    }
}
