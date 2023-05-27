package tm.store.meninki.api.request;

import java.util.ArrayList;

import tm.store.meninki.api.data.PersonalCharacterDto;

public class RequestUpdatePCh {
    ArrayList<PersonalCharacterDto> personalCharacteristics;

    public ArrayList<PersonalCharacterDto> getPersonalCharacteristics() {
        return personalCharacteristics;
    }

    public void setPersonalCharacteristics(ArrayList<PersonalCharacterDto> personalCharacteristics) {
        this.personalCharacteristics = personalCharacteristics;
    }
    //    {
//        "personalCharacteristics": [
//        {
//            "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
//                "count": 0,
//                "priceBonus": 0,
//                "price": 0,
//                "discountPrice": 0,
//                "productId": "3fa85f64-5717-4562-b3fc-2c963f66afa6"
//        }
//  ]
//    }
}
