package automation;



public class Model {

    private String mobileNo;
    private String fields;
    private String etbNtbFlag;
    private String businessVintage;
    private String numOpenHd;
    private String totOpenMort;
    private String numOpenPr;
    private String industryType	;
    private String worstAgingL6mCc;
    private String worstAgingL6mOd;
    private String worstAgingL6mOt;
    private String worstAgingL6mPl;
    private String riskGrade;
    private String numEnquiriesCcL3m;
    private String numOpenUnsecPl;
    private String ciIvprMaxDelq;
    private String numPartPayCcL3m;
    private String numFullPymtyL3m;
    private String dti;
    private String tue;
    private String loanAmount_system_bil;
    private String exposure_cccis_bil;
    private String entityType;
    private String bod_ebbs_bil;
    private String loan_rls_bil;
    private String ccplGuarantor_ccms_bil;
    private String sto;
    private String tueBorrowingEntity_user_bil;
    private String incomeFactor;
    private String pclGuarantor_icm_bil;
    private String productType;
    private String relatedPartyRLSLoan;
    private String bilAmount;
    private String wclAmount;
    private String otherIncomeRelatedParty_user_bil;
    //this field is for ntb
    private String numEnquiriesPlL3m;
    private String numFullPymtyL6m;
    private String totOpenMv;

    public String getOtherIncomeRelatedParty_user_bil() {
        return otherIncomeRelatedParty_user_bil;
    }

    public void setOtherIncomeRelatedParty_user_bil(String otherIncomeRelatedParty_user_bil) {
        this.otherIncomeRelatedParty_user_bil = otherIncomeRelatedParty_user_bil;
    }

    private String numCashAdvyL6m;
    private String numOpenMl;
    private String delqOffUsUnsecMob;
    private String numOpenHl;
    private String numPartPayCcL6m;

    public Model(){

    }

    public String getNumFullPymtyL6m() {
        return numFullPymtyL6m;
    }

    public void setNumFullPymtyL6m(String numFullPymtyL6m) {
        this.numFullPymtyL6m = numFullPymtyL6m;
    }

    public String getTotOpenMv() {
        return totOpenMv;
    }

    public void setTotOpenMv(String totOpenMv) {
        this.totOpenMv = totOpenMv;
    }

    public String getNumCashAdvyL6m() {
        return numCashAdvyL6m;
    }

    public void setNumCashAdvyL6m(String numCashAdvyL6m) {
        this.numCashAdvyL6m = numCashAdvyL6m;
    }

    public String getNumOpenMl() {
        return numOpenMl;
    }

    public void setNumOpenMl(String numOpenMl) {
        this.numOpenMl = numOpenMl;
    }

    public String getDelqOffUsUnsecMob() {
        return delqOffUsUnsecMob;
    }

    public void setDelqOffUsUnsecMob(String delqOffUsUnsecMob) {
        this.delqOffUsUnsecMob = delqOffUsUnsecMob;
    }

    public String getNumOpenHl() {
        return numOpenHl;
    }

    public void setNumOpenHl(String numOpenHl) {
        this.numOpenHl = numOpenHl;
    }

    public String getNumPartPayCcL6m() {
        return numPartPayCcL6m;
    }

    public void setNumPartPayCcL6m(String numPartPayCcL6m) {
        this.numPartPayCcL6m = numPartPayCcL6m;
    }

    public Model(String mobileNo, String fields, String etbNtbFlag, String businessVintage, String numOpenHd, String totOpenMort, String numOpenPr, String industryType, String worstAgingL6mCc, String worstAgingL6mOd, String worstAgingL6mOt, String worstAgingL6mPl, String riskGrade, String numEnquiriesCcL3m, String numEnquiriesPlL3m, String numOpenUnsecPl, String ciIvprMaxDelq, String numPartPayCcL3m, String numFullPymtyL3m, String dti, String tue, String loanAmount_system_bil, String exposure_cccis_bil, String entityType, String bod_ebbs_bil, String loan_rls_bil, String ccplGuarantor_ccms_bil, String sto, String tueBorrowingEntity_user_bil, String incomeFactor, String pclGuarantor_icm_bil, String productType, String relatedPartyRLSLoan, String bilAmount, String wclAmount, String otherIncomeRelatedParty_user_bil) {
        this.mobileNo = mobileNo;
        this.fields = fields;
        this.etbNtbFlag = etbNtbFlag;
        this.businessVintage = businessVintage;
        this.numOpenHd = numOpenHd;
        this.totOpenMort = totOpenMort;
        this.numOpenPr = numOpenPr;
        this.industryType = industryType;
        this.worstAgingL6mCc = worstAgingL6mCc;
        this.worstAgingL6mOd = worstAgingL6mOd;
        this.worstAgingL6mOt = worstAgingL6mOt;
        this.worstAgingL6mPl = worstAgingL6mPl;
        this.riskGrade = riskGrade;
        this.numEnquiriesCcL3m = numEnquiriesCcL3m;
        this.numEnquiriesPlL3m = numEnquiriesPlL3m;
        this.numOpenUnsecPl = numOpenUnsecPl;
        this.ciIvprMaxDelq = ciIvprMaxDelq;
        this.numPartPayCcL3m = numPartPayCcL3m;
        this.numFullPymtyL3m = numFullPymtyL3m;
        this.dti = dti;
        this.tue = tue;
        this.loanAmount_system_bil = loanAmount_system_bil;
        this.exposure_cccis_bil = exposure_cccis_bil;
        this.entityType = entityType;
        this.bod_ebbs_bil = bod_ebbs_bil;
        this.loan_rls_bil = loan_rls_bil;
        this.ccplGuarantor_ccms_bil = ccplGuarantor_ccms_bil;
        this.sto = sto;
        this.tueBorrowingEntity_user_bil = tueBorrowingEntity_user_bil;
        this.incomeFactor = incomeFactor;
        this.pclGuarantor_icm_bil = pclGuarantor_icm_bil;
        this.productType = productType;
        this.relatedPartyRLSLoan = relatedPartyRLSLoan;
        this.bilAmount = bilAmount;
        this.wclAmount = wclAmount;
        this.otherIncomeRelatedParty_user_bil = otherIncomeRelatedParty_user_bil;
    }

    public String getRelatedPartyRLSLoan() {
        return relatedPartyRLSLoan;
    }

    public void setRelatedPartyRLSLoan(String relatedPartyRLSLoan) {
        this.relatedPartyRLSLoan = relatedPartyRLSLoan;
    }

    public String getBilAmount() {
        return bilAmount;
    }

    public void setBilAmount(String bilAmount) {
        this.bilAmount = bilAmount;
    }

    public String getWclAmount() {
        return wclAmount;
    }

    public void setWclAmount(String wclAmount) {
        this.wclAmount = wclAmount;
    }

    public String getMobileNo() {
        return mobileNo;
    }
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getEtbNtbFlag() {
        return etbNtbFlag;
    }

    public void setEtbNtbFlag(String etbNtbFlag) {
        this.etbNtbFlag = etbNtbFlag;
    }

    public String getBusinessVintage() {
        return businessVintage;
    }

    public void setBusinessVintage(String businessVintage) {
        this.businessVintage = businessVintage;
    }

    public String getNumOpenHd() {
        return numOpenHd;
    }

    public void setNumOpenHd(String numOpenHd) {
        this.numOpenHd = numOpenHd;
    }

    public String getTotOpenMort() {
        return totOpenMort;
    }

    public void setTotOpenMort(String totOpenMort) {
        this.totOpenMort = totOpenMort;
    }

    public String getNumOpenPr() {
        return numOpenPr;
    }

    public void setNumOpenPr(String numOpenPr) {
        this.numOpenPr = numOpenPr;
    }

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    public String getWorstAgingL6mCc() {
        return worstAgingL6mCc;
    }

    public void setWorstAgingL6mCc(String worstAgingL6mCc) {
        this.worstAgingL6mCc = worstAgingL6mCc;
    }

    public String getWorstAgingL6mOd() {
        return worstAgingL6mOd;
    }

    public void setWorstAgingL6mOd(String worstAgingL6mOd) {
        this.worstAgingL6mOd = worstAgingL6mOd;
    }

    public String getWorstAgingL6mOt() {
        return worstAgingL6mOt;
    }

    public void setWorstAgingL6mOt(String worstAgingL6mOt) {
        this.worstAgingL6mOt = worstAgingL6mOt;
    }

    public String getWorstAgingL6mPl() {
        return worstAgingL6mPl;
    }

    public void setWorstAgingL6mPl(String worstAgingL6mPl) {
        this.worstAgingL6mPl = worstAgingL6mPl;
    }

    public String getRiskGrade() {
        return riskGrade;
    }

    public void setRiskGrade(String riskGrade) {
        this.riskGrade = riskGrade;
    }

    public String getNumEnquiriesCcL3m() {
        return numEnquiriesCcL3m;
    }

    public void setNumEnquiriesCcL3m(String numEnquiriesCcL3m) {
        this.numEnquiriesCcL3m = numEnquiriesCcL3m;
    }

    public String getNumEnquiriesPlL3m() {
        return numEnquiriesPlL3m;
    }

    public void setNumEnquiriesPlL3m(String numEnquiriesPlL3m) {
        this.numEnquiriesPlL3m = numEnquiriesPlL3m;
    }

    public String getNumOpenUnsecPl() {
        return numOpenUnsecPl;
    }

    public void setNumOpenUnsecPl(String numOpenUnsecPl) {
        this.numOpenUnsecPl = numOpenUnsecPl;
    }

    public String getCiIvprMaxDelq() {
        return ciIvprMaxDelq;
    }

    public void setCiIvprMaxDelq(String ciIvprMaxDelq) {
        this.ciIvprMaxDelq = ciIvprMaxDelq;
    }

    public String getNumPartPayCcL3m() {
        return numPartPayCcL3m;
    }

    public void setNumPartPayCcL3m(String numPartPayCcL3m) {
        this.numPartPayCcL3m = numPartPayCcL3m;
    }

    public String getNumFullPymtyL3m() {
        return numFullPymtyL3m;
    }

    public void setNumFullPymtyL3m(String numFullPymtyL3m) {
        this.numFullPymtyL3m = numFullPymtyL3m;
    }

    public String getDti() {
        return dti;
    }

    public void setDti(String dti) {
        this.dti = dti;
    }

    public String getTue() {
        return tue;
    }

    public void setTue(String tue) {
        this.tue = tue;
    }

    public String getLoanAmount_system_bil() {
        return loanAmount_system_bil;
    }

    public void setLoanAmount_system_bil(String loanAmount_system_bil) {
        this.loanAmount_system_bil = loanAmount_system_bil;
    }

    public String getExposure_cccis_bil() {
        return exposure_cccis_bil;
    }

    public void setExposure_cccis_bil(String exposure_cccis_bil) {
        this.exposure_cccis_bil = exposure_cccis_bil;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getBod_ebbs_bil() {
        return bod_ebbs_bil;
    }

    public void setBod_ebbs_bil(String bod_ebbs_bil) {
        this.bod_ebbs_bil = bod_ebbs_bil;
    }

    public String getLoan_rls_bil() {
        return loan_rls_bil;
    }

    public void setLoan_rls_bil(String loan_rls_bil) {
        this.loan_rls_bil = loan_rls_bil;
    }

    public String getCcplGuarantor_ccms_bil() {
        return ccplGuarantor_ccms_bil;
    }

    public void setCcplGuarantor_ccms_bil(String ccplGuarantor_ccms_bil) {
        this.ccplGuarantor_ccms_bil = ccplGuarantor_ccms_bil;
    }

    public String getSto() {
        return sto;
    }

    public void setSto(String sto) {
        this.sto = sto;
    }

    public String getTueBorrowingEntity_user_bil() {
        return tueBorrowingEntity_user_bil;
    }

    public void setTueBorrowingEntity_user_bil(String tueBorrowingEntity_user_bil) {
        this.tueBorrowingEntity_user_bil = tueBorrowingEntity_user_bil;
    }

    public String getIncomeFactor() {
        return incomeFactor;
    }

    public void setIncomeFactor(String incomeFactor) {
        this.incomeFactor = incomeFactor;
    }

    public String getPclGuarantor_icm_bil() {
        return pclGuarantor_icm_bil;
    }

    public void setPclGuarantor_icm_bil(String pclGuarantor_icm_bil) {
        this.pclGuarantor_icm_bil = pclGuarantor_icm_bil;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    @Override
    public String toString() {
        return "Model{" +
                "mobileNo='" + mobileNo + '\'' +
                ", fields='" + fields + '\'' +
                ", etbNtbFlag='" + etbNtbFlag + '\'' +
                ", businessVintage='" + businessVintage + '\'' +
                ", numOpenHd='" + numOpenHd + '\'' +
                ", totOpenMort='" + totOpenMort + '\'' +
                ", numOpenPr='" + numOpenPr + '\'' +
                ", industryType='" + industryType + '\'' +
                ", worstAgingL6mCc='" + worstAgingL6mCc + '\'' +
                ", worstAgingL6mOd='" + worstAgingL6mOd + '\'' +
                ", worstAgingL6mOt='" + worstAgingL6mOt + '\'' +
                ", worstAgingL6mPl='" + worstAgingL6mPl + '\'' +
                ", riskGrade='" + riskGrade + '\'' +
                ", numEnquiriesCcL3m='" + numEnquiriesCcL3m + '\'' +
                ", numEnquiriesPlL3m='" + numEnquiriesPlL3m + '\'' +
                ", numOpenUnsecPl='" + numOpenUnsecPl + '\'' +
                ", ciIvprMaxDelq='" + ciIvprMaxDelq + '\'' +
                ", numPartPayCcL3m='" + numPartPayCcL3m + '\'' +
                ", numFullPymtyL3m='" + numFullPymtyL3m + '\'' +
                ", dti='" + dti + '\'' +
                ", tue='" + tue + '\'' +
                ", loanAmount_system_bil='" + loanAmount_system_bil + '\'' +
                ", exposure_cccis_bil='" + exposure_cccis_bil + '\'' +
                ", entityType='" + entityType + '\'' +
                ", bod_ebbs_bil='" + bod_ebbs_bil + '\'' +
                ", loan_rls_bil='" + loan_rls_bil + '\'' +
                ", ccplGuarantor_ccms_bil='" + ccplGuarantor_ccms_bil + '\'' +
                ", sto='" + sto + '\'' +
                ", tueBorrowingEntity_user_bil='" + tueBorrowingEntity_user_bil + '\'' +
                ", incomeFactor='" + incomeFactor + '\'' +
                ", pclGuarantor_icm_bil='" + pclGuarantor_icm_bil + '\'' +
                ", productType='" + productType + '\'' +
                '}';
    }
}
