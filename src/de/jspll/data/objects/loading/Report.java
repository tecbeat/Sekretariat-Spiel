package de.jspll.data.objects.loading;

public class Report implements ProgressReporter {
    private float count;
    private float initalCount;
    private float percentage;

    public Report(int count){
        this.initalCount = count * 60;
        this.count = count * 60;
    }

    public Report(){

    }

    public void setCount(int count){
        this.initalCount = count;
        this.count = count;
    }

    public void update(){
        this.count--;
        percentage = (1-(count/initalCount));

        if(count<=0) {
            percentage = -1;
        }

        System.out.println(count);
        System.out.println(percentage);
    }

    @Override
    public float getProgress() {
        return percentage;
    }
}
