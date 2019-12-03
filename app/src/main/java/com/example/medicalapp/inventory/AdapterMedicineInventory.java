package com.example.medicalapp.inventory;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalapp.CommonFunctionsClass;
import com.example.medicalapp.Constants;
import com.example.medicalapp.R;

import java.util.List;

public class AdapterMedicineInventory extends RecyclerView.Adapter<AdapterMedicineInventory.Holder> {

    private static final String TAG = AdapterMedicineInventory.class.getName();
    private Context context;
    private List<MedInventoryModel> medInventoryModelList;
    private MedInventoryDatabaseManagement medInventoryDatabaseManagement;

    public AdapterMedicineInventory(Context context, List<MedInventoryModel> medInventoryModelList) {
        this.context = context;
        this.medInventoryModelList = medInventoryModelList;
        medInventoryDatabaseManagement = new MedInventoryDatabaseManagement(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_med_inventory_card, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {

        final MedInventoryModel medInventoryModel = medInventoryModelList.get(holder.getAdapterPosition());

        holder.cardStoredMed.setBackgroundColor(context.getResources().getColor(R.color.meColor));

        holder.medNamePlace.setText(medInventoryModel.getMedName());
        holder.medRemainingStockPlace.setText("" + medInventoryModel.getMedStock());
        holder.medDailyTakenPlace.setText("" + medInventoryModel.getMedPerDay());
        holder.medAddedAtPlace.setText(medInventoryModel.getMedAddedAt());
        holder.medDescriptionPlace.setText(medInventoryModel.getMedDetail());
        holder.medExpiredAtPlace.setText(medInventoryModel.getMedExpiryDate());

        holder.cardStoredMed.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.cardStoredMed);
                popup.inflate(R.menu.list_menu_inventory);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                //handle menu1 click
                                medInventoryModelList.remove(holder.getAdapterPosition());

                                if (medInventoryDatabaseManagement.deleteData(MedInventoryDatabaseHelper.TABLE_MEDICINE_DETAILS, MedInventoryDatabaseHelper.MEDICINE_ID, medInventoryModel.getmId())) {
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    notifyItemRangeChanged(holder.getAdapterPosition(), medInventoryModelList.size());
                                }

                                break;
                            case R.id.update:
                                FragmentAddMedInInventory fragmentAddMedInInventory = new FragmentAddMedInInventory();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(Constants.MED_INVENTORY_OBJ, medInventoryModel);
                                fragmentAddMedInInventory.setArguments(bundle);
                                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_inventory, fragmentAddMedInInventory).addToBackStack(null).commit();
                                break;
                            default:
                                Log.e(TAG, "onMenuItemClick: ");
                        }
                        return true;
                    }
                });
                //displaying the popup
                popup.show();
                return true;
            }
        });


        if (CommonFunctionsClass.isOutdated(medInventoryModel.getMedExpiryDate())) {
            holder.medExpiredAtPlace.setTextColor(context.getResources().getColor(R.color.warning));
        }


    }

    @Override
    public int getItemCount() {
        return medInventoryModelList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private CardView cardStoredMed;
        private TextView medNamePlace, medRemainingStockPlace, medAddedAtPlace, medExpiredAtPlace, medDescriptionPlace, medDailyTakenPlace;

        public Holder(@NonNull View itemView) {
            super(itemView);

            cardStoredMed = itemView.findViewById(R.id.cardStoredMed);

            medNamePlace = itemView.findViewById(R.id.medNamePlace);
            medRemainingStockPlace = itemView.findViewById(R.id.medRemainingStockPlace);
            medAddedAtPlace = itemView.findViewById(R.id.medAddedAtPlace);
            medExpiredAtPlace = itemView.findViewById(R.id.medExpiredAtPlace);
            medDescriptionPlace = itemView.findViewById(R.id.medDescriptionPlace);
            medDailyTakenPlace = itemView.findViewById(R.id.medDailyTakenPlace);

        }
    }
}
