package com.su.temperature;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class MainActivity extends Activity implements OnClickListener {

    private static final String TAG = "sufadi";

    private final static int Visible_XRange_Maximum = 60;

    private final static int TYPE_REAL = 0;
    private final static int TYPE_PREDICT = 1;

    private Button btn_clear;
    private Button btn_auto;
    private Button btn_real_add;
    private Button btn_predict_add;
    private LineChart mLineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initValue();
        initLisener();
    }

    private void initView() {
        btn_clear = (Button) findViewById(R.id.btn_clear);
        btn_auto = (Button) findViewById(R.id.btn_auto);
        btn_real_add = (Button) findViewById(R.id.btn_real_add);
        btn_predict_add = (Button) findViewById(R.id.btn_predict_add);
        mLineChart = (LineChart) findViewById(R.id.lineChart);
    }

    private void initValue() {
        initLineChart();
        initXAxis();
        initYAxisRight();
        initYAxisLeft();
        initLeftYAxisLimitLine();
    }

    private void initLisener() {
        btn_real_add.setOnClickListener(this);
        btn_predict_add.setOnClickListener(this);
        btn_auto.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.btn_real_add:
            addEntry(TYPE_REAL);
            break;
        case R.id.btn_predict_add:
            addEntry(TYPE_PREDICT);
            break;
        case R.id.btn_auto:
            feedMultiple();
            break;
        case R.id.btn_clear:
            clear();
            break;
        default:
            break;
        }

    }

    private void initLineChart() {
        // ��ʾ�߽�
        mLineChart.setDrawBorders(true);
        // ������ɫ
        mLineChart.setBackgroundColor(0x80000000);
        // ���񱳾�
        mLineChart.setGridBackgroundColor(0x80000000);
        // ����/������ͼ������п��ܵĴ���������
        mLineChart.setTouchEnabled(false);
        // �趨x�����ɼ�����Χ�Ĵ�С
        mLineChart.setVisibleXRangeMaximum(Visible_XRange_Maximum);
        // �趨y�����ɼ�����Χ�Ĵ�С
        mLineChart.setVisibleYRangeMaximum(100f, YAxis.AxisDependency.LEFT);
        mLineChart.setVisibleXRangeMinimum(0f);

        // ���ñ�ǩ����
        mLineChart.setDescription("");
        // ���ñ�ǩ��ɫ
        mLineChart.setDescriptionColor(Color.WHITE);

        // Ϊchart��ӿ�����
        mLineChart.setData(getDefaultData());
    }

    private LineData getDefaultData() {
        LineData mLineData = new LineData();
        return mLineData;
    }

    private void initXAxis() {
        // �õ�X��
        XAxis xAxis = mLineChart.getXAxis();
        // �õ�X���λ��
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // ����X����
        xAxis.setSpaceBetweenLabels(10);
        // ����������
        xAxis.setDrawGridLines(true);
        // ���ñ�ǩ�ı�
        xAxis.setTextColor(Color.WHITE);

        // �����ı���ʾ
        xAxis.setValueFormatter(new XAxisValueFormatter() {

            @Override
            public String getXValue(String original, int index, ViewPortHandler viewPortHandler) {
                // Log.d("sufadi", "original = " + original + ", index = " +
                // index);
                return String.format("%d s", index);
            }
        });
    }

    private void initYAxisRight() {
        // �õ�Y���Ҳಢ����ʾ
        YAxis rightYAxis = mLineChart.getAxisRight();
        rightYAxis.setEnabled(false);
    }

    private void initYAxisLeft() {
        // �õ�Y�����
        YAxis leftYAxis = mLineChart.getAxisLeft();
        // ���ñ�ǩ�ı�
        leftYAxis.setTextColor(Color.WHITE);
        leftYAxis.setAxisMaxValue(100f);
        leftYAxis.setAxisMinValue(0f);
        // ˵�����ֻ�ͼ�����ʾ
        leftYAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftYAxis.setValueFormatter(new YAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                return String.format("%d ��", (int) value);
            }
        });
        // Y���ܻ�߳�X��һ�㣬��û�д�0�㿪ʼ�������Ҫ��Y���������
        leftYAxis.setAxisMinValue(0f);
    }

    /**
     * ������
     */
    private void initLeftYAxisLimitLine() {
        // Y�����������
        LimitLine limitLine = new LimitLine(40, "");
        limitLine.setTextColor(Color.RED); // ��ɫ
        limitLine.setLineColor(Color.RED);
        mLineChart.getAxisLeft().addLimitLine(limitLine);
    }

    /**
     * ����ͼ����������
     */
    private LineDataSet createLineDataSet(int Type) {
        LineDataSet mLineDataSet = new LineDataSet(null, "ʵ���¶�");
        switch (Type) {
        case TYPE_PREDICT:
            mLineDataSet.setLabel("Ԥ���¶�");
            mLineDataSet.setColor(Color.GREEN);
            break;
        case TYPE_REAL:
            mLineDataSet.setLabel("ʵ���¶�");
            mLineDataSet.setColor(Color.YELLOW);
            break;

        default:
            break;
        }
        // �����������Ϊ׼
        mLineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        // �����ı���С
        mLineDataSet.setValueTextSize(12f);
        // ��������ֵ��Բ����ʵ�Ļ��ǿ���
        mLineDataSet.setDrawCircleHole(false);
        // ��������ֵ��Բ���С
        mLineDataSet.setCircleSize(0);
        // ��ģʽΪԲ�����ߣ�Ĭ�����ߣ�
        mLineDataSet.setDrawCubic(true);
        // �����Ƿ���ʾ��ֵ
        mLineDataSet.setDrawValues(false);
        // ������ɫ
        return mLineDataSet;
    }

    private void addEntry(int Type) {
        LineData lineData = mLineChart.getData();

        if (null == lineData) {
            return;
        }

        LineDataSet dataSet = null;
        switch (Type) {
        case TYPE_PREDICT:
            dataSet = lineData.getDataSetByLabel("Ԥ���¶�", true);
            break;
        case TYPE_REAL:
            dataSet = lineData.getDataSetByLabel("ʵ���¶�", true);
            break;

        default:
            break;
        }

        if (dataSet == null) {
            dataSet = createLineDataSet(Type);
            lineData.addDataSet(dataSet);
        }

        // ����Ҫע�⣬x���index�Ǵ��㿪ʼ��
        // ����index=2����ôgetEntryCount()�͵���3��
        int count = dataSet.getEntryCount();
        // add a new x-value first ���д��벻����
        lineData.addXValue(count + "");

        float yValues = (float) (Math.random() * 60 + 10);
        lineData.addEntry(new Entry(yValues, count), lineData.getIndexOfDataSet(dataSet));
        mLineChart.notifyDataSetChanged();
        mLineChart.setVisibleXRangeMaximum(Visible_XRange_Maximum);
        mLineChart.moveViewToX(dataSet.getEntryCount());
        mLineChart.moveViewTo(dataSet.getEntryCount() - Visible_XRange_Maximum, 55f, AxisDependency.LEFT);
        Log.d(TAG, "set.getEntryCount()=" + dataSet.getEntryCount() + ", lineData.getXValCount() = " + lineData.getXValCount());
    }

    private Thread thread;
    private boolean isReturn = false;

    private void feedMultiple() {
        if (thread != null)
            thread.interrupt();

        isReturn = false;
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                isReturn = !isReturn;
                if (isReturn) {
                    addEntry(TYPE_REAL);
                } else {
                    addEntry(TYPE_PREDICT);
                }
            }
        };

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    // Don't generate garbage runnables inside the loop.
                    runOnUiThread(runnable);
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }
    
    private void clear() {
        mLineChart.clear();
        mLineChart.setData(new LineData());
        mLineChart.invalidate();
        initLineChart();
    }
}
