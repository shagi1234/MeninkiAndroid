package tm.store.meninki.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import tm.store.meninki.data.CharactersDto;
import tm.store.meninki.data.SelectedMedia;
import tm.store.meninki.databinding.ItemPersonalCharactersBinding;
import tm.store.meninki.utils.Dialog;
import tm.store.meninki.utils.Lists;

import java.util.ArrayList;

public class AdapterPersonalCharacters extends RecyclerView.Adapter<AdapterPersonalCharacters.CharactersHolder> {
    private ArrayList<CharactersDto> characters;
    public static final String CHARACTER_TEXT = "character_text";
    public static final String CHARACTER_IMAGE = "character_image";
    public static final String CHARACTER_COLOR = "character_color";
    private Context context;

    public AdapterPersonalCharacters(Context context, ArrayList<CharactersDto> characters) {
        this.context = context;
        this.characters = characters;
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
        if (characters == null) {
            return 0;
        }
        return characters.size();
    }

    private void removeAt(int position) {
        try {
            characters.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, SelectedMedia.getArrayList().size());
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

            switch (characters.get(getAdapterPosition()).getType()) {
                case CHARACTER_TEXT:
                    setRecyclerText();
                    break;
                case CHARACTER_IMAGE:
                    setRecyclerImage();
                    break;
                case CHARACTER_COLOR:
                    setRecyclerColor();
                    break;

            }

            b.clearCharacter.setOnClickListener(v -> removeAt(getAdapterPosition()));

            b.edtName.setOnClickListener(v -> {
                //change title
                Dialog dialog = new Dialog();
                dialog.showDialog(context);
                dialog.yesBtn.setOnClickListener(v1 -> {
                    if (dialog.title.getText().toString().trim().length() == 0) {
                        Toast.makeText(context, "Your text is empty, please write something", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    b.nameCharacters.setText(dialog.title.getText().toString().trim());
                    dialog.dialog.dismiss();
                });
            });

        }

        private void setRecyclerColor() {
            AdapterCharColor adapter = new AdapterCharColor(context, AdapterCharPick.ADDABLE);
            b.recItem.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            b.recItem.setAdapter(adapter);
        }

        private void setRecyclerImage() {
            AdapterCharImage adapter = new AdapterCharImage(context, AdapterCharPick.ADDABLE);
            b.recItem.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            b.recItem.setAdapter(adapter);

            adapter.setImageUrl(null);
        }

        private void setRecyclerText() {
            AdapterCharPick adapter = new AdapterCharPick(context, AdapterCharPick.ADDABLE);
            b.recItem.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            b.recItem.setAdapter(adapter);

            adapter.setPicks(Lists.getPicks());
        }
    }
}
