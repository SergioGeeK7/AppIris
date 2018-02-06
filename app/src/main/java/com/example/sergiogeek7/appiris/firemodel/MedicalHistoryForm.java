package com.example.sergiogeek7.appiris.firemodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.android.databinding.library.baseAdapters.BR;
/**
 * Created by sergiogeek7 on 2/02/18.
 */

public class MedicalHistoryForm extends BaseObservable {

    private String medicalTreatment;
    private String drugs;
    private String medicine;
    private String surgery;
    private String childhoodAilments;
    private String currentAilments;
    private String parentsAilments;
    private boolean beef;
    private boolean pork;
    private boolean chicken;
    private boolean sweet;
    private boolean salty;
    private boolean cold;
    private boolean hot;
    private String breakfastAliments;
    private String lunchAliments;
    private String dinnerAliments;
    private boolean constipation;
    private boolean gases;
    private boolean diarrhea;
    private boolean acidity;
    private boolean lackOfAppetite;
    private boolean cramps;
    private boolean dizziness;
    private boolean hotFlashes;
    private boolean varicoseUlcers;
    private boolean veinsVaricose;
    private boolean headaches;
    private boolean pangHead;
    private boolean pangBody;
    private boolean coughDay;
    private boolean coughNight;
    private boolean suffocation;
    private boolean phlegm;
    private boolean fever;
    private boolean chestSore;
    private boolean throatSore;
    private boolean feet;
    private boolean legs;
    private boolean knees;
    private boolean back;
    private boolean waist;
    private boolean hands;
    private boolean arms;
    private boolean shoulders;
    private boolean juncturesRight;
    private boolean juncturesLeft;
    private boolean fewUrine;
    private boolean abundantUrine;
    private boolean burningUrine;
    private boolean painUrine;
    private boolean fewPeriod;
    private boolean monthlyPeriod;
    private boolean dailyPeriod;
    private boolean colic;
    private boolean useBirthControl;
    private String childrenNumber;
    private boolean cesarean;
    private boolean natural;
    private boolean hasJunctures;
    private boolean hasCough;

    public String getUserUId() {
        return userUId;
    }

    public void setUserUId(String userUId) {
        this.userUId = userUId;
    }

    private String userUId;

    @Bindable
    public boolean isHasCough() {
        return hasCough;
    }

    public void setHasCough(boolean hasCough) {
        this.hasCough = hasCough;
        notifyPropertyChanged(BR.hasCough);
        if(!hasCough){
            setCoughDay(false);
            setCoughNight(false);
        }
    }

    public MedicalHistoryForm(){

    }

    @Bindable
    public String getMedicalTreatment() {
        return medicalTreatment;
    }

    public void setMedicalTreatment(String medicalTreatment) {
        this.medicalTreatment = medicalTreatment;
        notifyPropertyChanged(BR.medicalTreatment);
    }

    @Bindable
    public String getDrugs() {
        return drugs;
    }

    public void setDrugs(String drugs) {
        this.drugs = drugs;
        notifyPropertyChanged(BR.drugs);
    }
    @Bindable
    public String getMedicine() {
        return medicine;
    }


    public void setMedicine(String medicine) {
        this.medicine = medicine;
        notifyPropertyChanged(BR.medicine);
    }

    @Bindable
    public String getSurgery() {
        return surgery;
    }

    public void setSurgery(String surgery) {
        this.surgery = surgery;
        notifyPropertyChanged(BR.surgery);
    }
    @Bindable
    public String getChildhoodAilments() {
        return childhoodAilments;
    }

    public void setChildhoodAilments(String childhoodAilments) {
        this.childhoodAilments = childhoodAilments;
        notifyPropertyChanged(BR.childhoodAilments);
    }
    @Bindable
    public String getCurrentAilments() {
        return currentAilments;
    }

    public void setCurrentAilments(String currentAilments) {
        this.currentAilments = currentAilments;
        notifyPropertyChanged(BR.currentAilments);
    }

    @Bindable
    public String getParentsAilments() {
        return parentsAilments;
    }

    public void setParentsAilments(String parentsAilments) {
        this.parentsAilments = parentsAilments;
        notifyPropertyChanged(BR.parentsAilments);
    }

    @Bindable
    public boolean isBeef() {
        return beef;
    }

    public void setBeef(boolean beef) {
        this.beef = beef;
        notifyPropertyChanged(BR.beef);
    }

    @Bindable
    public boolean isPork() {
        return pork;
    }

    public void setPork(boolean pork) {
        this.pork = pork;
        notifyPropertyChanged(BR.pork);
    }
    @Bindable
    public boolean isChicken() {
        return chicken;
    }

    public void setChicken(boolean chicken) {
        this.chicken = chicken;
        notifyPropertyChanged(BR.chicken);
    }

    @Bindable
    public boolean isSweet() {
        return sweet;
    }

    public void setSweet(boolean sweet) {
        this.sweet = sweet;
        notifyPropertyChanged(BR.sweet);
    }
    @Bindable
    public boolean isSalty() {
        return salty;
    }

    public void setSalty(boolean salty) {
        this.salty = salty;
        notifyPropertyChanged(BR.salty);
    }
    @Bindable
    public boolean isCold() {
        return cold;
    }

    public void setCold(boolean cold) {
        this.cold = cold;
        notifyPropertyChanged(BR.cold);
    }
    @Bindable
    public boolean isHot() {
        return hot;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
        notifyPropertyChanged(BR.hot);
    }
    @Bindable
    public String getBreakfastAliments() {
        return breakfastAliments;
    }

    public void setBreakfastAliments(String breakfastAliments) {
        this.breakfastAliments = breakfastAliments;
        notifyPropertyChanged(BR.breakfastAliments);
    }

    @Bindable
    public String getLunchAliments() {
        return lunchAliments;
    }

    public void setLunchAliments(String lunchAliments) {
        this.lunchAliments = lunchAliments;
        notifyPropertyChanged(BR.lunchAliments);
    }
    @Bindable
    public String getDinnerAliments() {
        return dinnerAliments;
    }

    public void setDinnerAliments(String dinnerAliments) {
        this.dinnerAliments = dinnerAliments;
        notifyPropertyChanged(BR.dinnerAliments);
    }
    @Bindable
    public boolean isConstipation() {
        return constipation;
    }

    public void setConstipation(boolean constipation) {
        this.constipation = constipation;
        notifyPropertyChanged(BR.constipation);
    }

    @Bindable
    public boolean isGases() {
        return gases;
    }

    public void setGases(boolean gases) {
        this.gases = gases;
        notifyPropertyChanged(BR.gases);
    }

    @Bindable
    public boolean isDiarrhea() {
        return diarrhea;
    }

    public void setDiarrhea(boolean diarrhea) {
        this.diarrhea = diarrhea;
        notifyPropertyChanged(BR.diarrhea);
    }
    @Bindable
    public boolean isAcidity() {
        return acidity;
    }

    public void setAcidity(boolean acidity) {
        this.acidity = acidity;
        notifyPropertyChanged(BR.acidity);
    }
    @Bindable
    public boolean isLackOfAppetite() {
        return lackOfAppetite;
    }

    public void setLackOfAppetite(boolean lackOfAppetite) {
        this.lackOfAppetite = lackOfAppetite;
        notifyPropertyChanged(BR.lackOfAppetite);
    }
    @Bindable
    public boolean isCramps() {
        return cramps;
    }

    public void setCramps(boolean cramps) {
        this.cramps = cramps;
        notifyPropertyChanged(BR.cramps);
    }

    @Bindable
    public boolean isDizziness() {
        return dizziness;
    }

    public void setDizziness(boolean dizziness) {
        this.dizziness = dizziness;
        notifyPropertyChanged(BR.dizziness);
    }
    @Bindable
    public boolean isHotFlashes() {
        return hotFlashes;
    }

    public void setHotFlashes(boolean hotFlashes) {
        this.hotFlashes = hotFlashes;
        notifyPropertyChanged(BR.hotFlashes);
    }
    @Bindable
    public boolean isVaricoseUlcers() {
        return varicoseUlcers;
    }

    public void setVaricoseUlcers(boolean varicoseUlcers) {
        this.varicoseUlcers = varicoseUlcers;
        notifyPropertyChanged(BR.varicoseUlcers);
    }
    @Bindable
    public boolean isVeinsVaricose() {
        return veinsVaricose;
    }

    public void setVeinsVaricose(boolean veinsVaricose) {
        this.veinsVaricose = veinsVaricose;
        notifyPropertyChanged(BR.veinsVaricose);
    }
    @Bindable
    public boolean isHeadaches() {
        return headaches;
    }

    public void setHeadaches(boolean headaches) {
        this.headaches = headaches;
        notifyPropertyChanged(BR.headaches);
    }
    @Bindable
    public boolean isPangHead() {
        return pangHead;
    }

    public void setPangHead(boolean pangHead) {
        this.pangHead = pangHead;
        notifyPropertyChanged(BR.pangHead);
    }
    @Bindable
    public boolean isPangBody() {
        return pangBody;
    }

    public void setPangBody(boolean pangBody) {
        this.pangBody = pangBody;
        notifyPropertyChanged(BR.pangBody);
    }
    @Bindable
    public boolean isCoughDay() {
        return coughDay;
    }

    public void setCoughDay(boolean coughDay) {
        this.coughDay = coughDay;
        notifyPropertyChanged(BR.coughDay);
    }
    @Bindable
    public boolean isCoughNight() {
        return coughNight;
    }

    public void setCoughNight(boolean coughNight) {
        this.coughNight = coughNight;
        notifyPropertyChanged(BR.coughNight);
    }
    @Bindable
    public boolean isSuffocation() {
        return suffocation;
    }

    public void setSuffocation(boolean suffocation) {
        this.suffocation = suffocation;
        notifyPropertyChanged(BR.suffocation);
    }

    @Bindable
    public boolean isPhlegm() {
        return phlegm;
    }

    public void setPhlegm(boolean phlegm) {
        this.phlegm = phlegm;
        notifyPropertyChanged(BR.phlegm);
    }
    @Bindable
    public boolean isFever() {
        return fever;
    }

    public void setFever(boolean fever) {
        this.fever = fever;
        notifyPropertyChanged(BR.fever);
    }
    @Bindable
    public boolean isChestSore() {
        return chestSore;
    }

    public void setChestSore(boolean chestSore) {
        this.chestSore = chestSore;
        notifyPropertyChanged(BR.chestSore);
    }

    @Bindable
    public boolean isThroatSore() {
        return throatSore;
    }

    public void setThroatSore(boolean throatSore) {
        this.throatSore = throatSore;
        notifyPropertyChanged(BR.throatSore);
    }

    @Bindable
    public boolean isFeet() {
        return feet;
    }

    public void setFeet(boolean feet) {
        this.feet = feet;
        notifyPropertyChanged(BR.feet);
    }
    @Bindable
    public boolean isLegs() {
        return legs;
    }

    public void setLegs(boolean legs) {
        this.legs = legs;
        notifyPropertyChanged(BR.legs);
    }
    @Bindable
    public boolean isKnees() {
        return knees;
    }

    public void setKnees(boolean knees) {
        this.knees = knees;
        notifyPropertyChanged(BR.knees);
    }
    @Bindable
    public boolean isBack() {
        return back;
    }

    public void setBack(boolean back) {
        this.back = back;
        notifyPropertyChanged(BR.back);
    }

    @Bindable
    public boolean isWaist() {
        return waist;
    }

    public void setWaist(boolean waist) {
        this.waist = waist;
        notifyPropertyChanged(BR.waist);
    }
    @Bindable
    public boolean isHands() {
        return hands;
    }

    public void setHands(boolean hands) {
        this.hands = hands;
        notifyPropertyChanged(BR.hands);
    }
    @Bindable
    public boolean isArms() {
        return arms;
    }

    public void setArms(boolean arms) {
        this.arms = arms;
        notifyPropertyChanged(BR.arms);
    }
    @Bindable
    public boolean isShoulders() {
        return shoulders;
    }

    public void setShoulders(boolean shoulders) {
        this.shoulders = shoulders;
        notifyPropertyChanged(BR.shoulders);
    }
    @Bindable
    public boolean isJuncturesRight() {
        return juncturesRight;
    }

    public void setJuncturesRight(boolean juncturesRight) {
        this.juncturesRight = juncturesRight;
        notifyPropertyChanged(BR.juncturesRight);
    }
    @Bindable
    public boolean isJuncturesLeft() {
        return juncturesLeft;
    }

    public void setJuncturesLeft(boolean juncturesLeft) {
        this.juncturesLeft = juncturesLeft;
        notifyPropertyChanged(BR.juncturesLeft);

    }
    @Bindable
    public boolean isFewUrine() {
        return fewUrine;
    }

    public void setFewUrine(boolean fewUrine) {
        this.fewUrine = fewUrine;
        notifyPropertyChanged(BR.fewUrine);
    }
    @Bindable
    public boolean isAbundantUrine() {
        return abundantUrine;
    }

    public void setAbundantUrine(boolean abundantUrine) {
        this.abundantUrine = abundantUrine;
        notifyPropertyChanged(BR.abundantUrine);
    }
    @Bindable
    public boolean isBurningUrine() {
        return burningUrine;
    }

    public void setBurningUrine(boolean burningUrine) {
        this.burningUrine = burningUrine;
        notifyPropertyChanged(BR.burningUrine);
    }
    @Bindable
    public boolean isPainUrine() {
        return painUrine;
    }

    public void setPainUrine(boolean painUrine) {
        this.painUrine = painUrine;
        notifyPropertyChanged(BR.painUrine);
    }
    @Bindable
    public boolean isDailyPeriod() {
        return dailyPeriod;
    }

    public void setDailyPeriod(boolean dailyPeriod) {
        this.dailyPeriod = dailyPeriod;
        notifyPropertyChanged(BR.dailyPeriod);
    }

    @Bindable
    public boolean isHasJunctures() {
        return hasJunctures;
    }

    public void setHasJunctures(boolean hasJunctures) {
        this.hasJunctures = hasJunctures;
        notifyPropertyChanged(BR.hasJunctures);
        if(!hasJunctures){
            setJuncturesLeft(false);
            setJuncturesRight(false);
        }
    }

    @Bindable
    public boolean isUseBirthControl() {
        return useBirthControl;
    }

    public void setUseBirthControl(boolean useBirthControl) {
        this.useBirthControl = useBirthControl;
        notifyPropertyChanged(BR.useBirthControl);
    }

    @Bindable
    public boolean isNatural() {
        return natural;
    }

    public void setNatural(boolean natural) {
        this.natural = natural;
        notifyPropertyChanged(BR.natural);
    }

    @Bindable
    public boolean isCesarean() {
        return cesarean;
    }

    public void setCesarean(boolean cesarean) {
        this.cesarean = cesarean;
        notifyPropertyChanged(BR.cesarean);
    }

    @Bindable
    public String getChildrenNumber() {
        return childrenNumber;
    }

    public void setChildrenNumber(String childrenNumber) {
        this.childrenNumber = childrenNumber;
        notifyPropertyChanged(BR.childrenNumber);
    }

    @Bindable
    public boolean isFewPeriod() {
        return fewPeriod;
    }

    public void setFewPeriod(boolean fewPeriod) {
        this.fewPeriod = fewPeriod;
        notifyPropertyChanged(BR.fewPeriod);
    }

    @Bindable
    public boolean isMonthlyPeriod() {
        return monthlyPeriod;
    }

    public void setMonthlyPeriod(boolean monthlyPeriod) {
        this.monthlyPeriod = monthlyPeriod;
        notifyPropertyChanged(BR.monthlyPeriod);
    }

    @Bindable
    public boolean isColic() {
        return colic;
    }

    @Bindable
    public void setColic(boolean colic) {
        this.colic = colic;
        notifyPropertyChanged(BR.colic);
    }

    @Override
    public String toString() {
        return "MedicalHistoryForm{" +
                "medicalTreatment='" + medicalTreatment + '\'' +
                ", drugs='" + drugs + '\'' +
                ", medicine='" + medicine + '\'' +
                ", surgery='" + surgery + '\'' +
                ", childhoodAilments='" + childhoodAilments + '\'' +
                ", currentAilments='" + currentAilments + '\'' +
                ", parentsAilments='" + parentsAilments + '\'' +
                ", beef=" + beef +
                ", pork=" + pork +
                ", chicken=" + chicken +
                ", sweet=" + sweet +
                ", salty=" + salty +
                ", cold=" + cold +
                ", hot=" + hot +
                ", breakfastAliments='" + breakfastAliments + '\'' +
                ", lunchAliments='" + lunchAliments + '\'' +
                ", dinnerAliments='" + dinnerAliments + '\'' +
                ", constipation=" + constipation +
                ", gases=" + gases +
                ", diarrhea=" + diarrhea +
                ", acidity=" + acidity +
                ", lackOfAppetite=" + lackOfAppetite +
                ", cramps=" + cramps +
                ", dizziness=" + dizziness +
                ", hotFlashes=" + hotFlashes +
                ", varicoseUlcers=" + varicoseUlcers +
                ", veinsVaricose=" + veinsVaricose +
                ", headaches=" + headaches +
                ", pangHead=" + pangHead +
                ", pangBody=" + pangBody +
                ", coughDay=" + coughDay +
                ", coughNight=" + coughNight +
                ", suffocation=" + suffocation +
                ", phlegm=" + phlegm +
                ", fever=" + fever +
                ", chestSore=" + chestSore +
                ", throatSore=" + throatSore +
                ", feet=" + feet +
                ", legs=" + legs +
                ", knees=" + knees +
                ", back=" + back +
                ", waist=" + waist +
                ", hands=" + hands +
                ", arms=" + arms +
                ", shoulders=" + shoulders +
                ", juncturesRight=" + juncturesRight +
                ", juncturesLeft=" + juncturesLeft +
                ", fewUrine=" + fewUrine +
                ", abundantUrine=" + abundantUrine +
                ", burningUrine=" + burningUrine +
                ", painUrine=" + painUrine +
                ", fewPeriod=" + fewPeriod +
                ", monthlyPeriod=" + monthlyPeriod +
                ", dailyPeriod=" + dailyPeriod +
                ", colic=" + colic +
                ", useBirthControl=" + useBirthControl +
                ", childrenNumber='" + childrenNumber + '\'' +
                ", cesarean=" + cesarean +
                ", natural=" + natural +
                '}';
    }
}
