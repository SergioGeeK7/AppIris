package com.example.sergiogeek7.appiris.opencv;

/**
 * Created by sergiogeek7 on 27/12/17.
 */

public class Psicosomaticas {

    private String [] bodyParts;
    private int SLICE = 15;

    public Psicosomaticas (String[] bodyParts){
        this.bodyParts = bodyParts;
    }

    public Psicosomaticas(){
        bodyParts = new String[]{
                "(F) pulmones, nervio vago, bronquios, timo, corazón, colon descendente y ascendente(izquierdo y derecho respectivamente), estomago.",
                "(E) axilas, pulmones, traquea, nervio vago, bronquios, aorta, aurículas del corazón, colon descendente y ascendente(izquierdo y derecho respectivamente), estomago.",
                "(D) cuello, nervio vago, glandula axiliar, clavicula, colon descendente y ascendente (izquierdo y derecho respectivamente), estomago.",
                "(C) cuero cabelludo, craneo, cerebro, cerebelo, oídos (timpano, nervio acústico). Glandula parótida, nervio vago, colon transversal, estomago.",
                "(B) cuero cabelludo, craneo, cerebro, cerebelo, nervio vago, colon tranversal, estomago.",
                "(A) cuero cabelludo, craneo, cerebro, septumlucidum, cuerpo calloso, glandula pineal, colon transversal, estomago",
                "(Y) cuero cabelludo, cráneo, cerebro, septum lucidum, cuerpo calloso, glándula pineal, colon transversal y estómago.",
                "(X) frente, cráneo, cerebro, colon transversal y estómago.",
                "(V) orificios nasales, ojos, colon transversal y estómago.",
                "(U) maxilar superior, lengua, amígdalas, nariz, intestino delgado y estomago.",
                "(T) torioides, laringe, glándulas linguales, lengua, cuerdas bocales, maxilar inferior, intestino delgado, estomago.",
                "(S) garganta: traquea, glandula tiroide y paratiroides, intestino delgado y estomago, esfínteres del cardias y el piloro(izquierdo y derecho respectivamente)",
                "(R) esófago, omoplato, traquea, intestino delgado y estomago.",
                "(Q) zona umbilical, columna vertebral, omoplato, intestino delgado y estomago.",
                "(P) pene, uretra, vejiga, uréter, coxis, columna vertebral, intestino delgado y estomago.",
                "(O) vagina, uretra, utero, vejiga, uréter, coxis, columna vertebral, intestino delgado y estomago.",
                "(N) vagina, uretra, utero, trampa de Falopio, riñones, caps suprarrenales, intestino y estomago, ano y recto(izquierdo) intestino delgado(derecho)",
                "(M) pies, piernas, trompas de Falopio, riñones, glándulas suprarrenales, intestino: sigmoide(izquierdo), ciego(derecho) y estomago.",
                "(L) Bajo abdomen, ingle, piernas, trompa, intestino y estomago. Colon sigmoideo (izquierdo). Apéndice. Ciego e ileon(derecho).",
                "(K) pelvis, ovarios, intestinos y estomago. Colon sigmoideo (izquierdo). Implica: apéndice, ciego e ileon(derecho).",
                "(J) ovarios o testículos, páncreas, intestinos y estomago. Colos descendente y ascendente(izquierdo y derecho respectivamente). Puede implicar el hígado, vesicula y conducto cístico en iris derecho(z. 3, 2 y 1)",
                "(I) manos, brazos. Bazo (izquierdo). Hígado y vesicula (derecho). Colon descendente y ascendente. (izquierdo y derecho respectivamente). Estomago.",
                "(H) caderas (ilion), caja toraxica, costillas inferiores, esternón, nervio vago. Plexo solar(izquierdo). Manos, brazos ½. Colon descendente y ascendente(izquierdo y derecho respectivamente). Estomago.",
                "(G) pleura, nervio vago, timo, corazón, glándulas mamarias, costillas superiores, colon descendente y ascendente(izquierdo y derecho respectivamente), estomago"
        };
    }

    public String getBodyPart(Shape shape, int reverse){
        int index = (int) Math.floor(shape.getAngle()/SLICE);
        String[] bodyParts = reverse == 1 ? reverseArray(this.bodyParts): this.bodyParts;
        return bodyParts[index];
    }

    private String[] reverseArray(String inputArray[]) {
        inputArray = inputArray.clone();
        String temp;
        for (int i = 0; i < inputArray.length/2; i++) {
            temp = inputArray[i];
            inputArray[i] = inputArray[inputArray.length-1-i];
            inputArray[inputArray.length-1-i] = temp;
        }
        return inputArray;
    }
}
