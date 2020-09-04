package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TextField start_spot;
    @FXML
    private TextField ready_queue;
    @FXML
    private TextField total_move;
    @FXML
    private ComboBox<String> schedule;
    @FXML
    private ComboBox<String> direction;
    @FXML
    private LineChart lineCT;
    @FXML
    private TableView<Column> Table;
    @FXML
    private TableColumn<Column, Integer> Table_num;
    @FXML
    private TableColumn<Column, Integer> Table_order;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    private int[] queue = null, arr, arr_temp,print;
    private int num;

    public void bt_queue(ActionEvent e) {
        Random start = new Random();
        num = start.nextInt(300) + 1;

        queue = new int[10];
        start_spot.setText(String.valueOf(num));
        for(int i = 0; i < queue.length; i++)
        {
            boolean check = true;
            num = start.nextInt(300) + 1;
            if(Integer.parseInt(start_spot.getText())==num) {
                i--;
                continue;
            }
            for(int j = 0;j<i;j++)
            {
                if(queue[j]==num) {
                    i--;
                    check = false;
                }
            }
            if(check)
                queue[i] = num;
        }

        String queue_lsit = Arrays.toString(queue);
        queue_lsit = queue_lsit.replaceAll("\\[","");
        queue_lsit = queue_lsit.replaceAll("\\]","");

        ready_queue.setText(queue_lsit);
    }

    public void bt_run(ActionEvent e) {
        String now_scheduler = schedule.getValue();
        String now_direction = direction.getValue();

        XYChart.Series<Number,Number> series = new XYChart.Series<>();
        lineCT.getData().clear();

        if(now_scheduler == null || now_direction == null)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("에러");
            alert.setHeaderText("정책 또는 헤더 방향이 설정되지 않았습니다.");
            alert.setContentText("정책과 헤더 방향을 선택해주세요.");

            alert.showAndWait();
        }

        else if(queue == null)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("에러");
            alert.setHeaderText("대기 중인 큐가 없습니다.");
            alert.setContentText("큐 생성 버튼을 누르고 실행하세요.");

            alert.showAndWait();
        }

        else {
                    int min, temp, index = 0;
                    int i, j;
                    int number = queue.length+1;
                    int startPoint = Integer.parseInt(start_spot.getText());
                    arr = new int[queue.length + 3];
                    arr_temp = new int[queue.length + 3];
                    print = new int[queue.length + 3];
                    System.arraycopy(queue, 0, arr, 1, queue.length);
                    arr[0] = startPoint;
                    System.arraycopy(arr, 0, arr_temp, 0, arr.length);

                    int cnt, cntP = 1;
                    if(!now_scheduler.equals("FCFS")) {
                        for (i = 1; i < arr_temp.length - 2; i++) {
                            min = 10000;
                            for (j = 1; j < arr_temp.length - 2; j++) {
                                if (arr[j] == -1) continue;
                                temp = Math.abs(arr_temp[i - 1] - arr[j]);
                                if (min > temp) {
                                    min = temp;
                                    index = j;
                                }
                            }
                            arr_temp[i] = arr[index];
                            arr[index] = -1;
                        }
                        for (; i < arr_temp.length; i++)
                            arr_temp[i] = -1;
                    }
                    if(now_scheduler.equals("FCFS"))
                    {
                        System.arraycopy(arr_temp, 0, print, 0, arr.length);
                    }

                    if(now_scheduler.equals("SSTF"))
                    {
                        System.arraycopy(arr_temp, 0, print, 0, arr.length);
                    }
                    if(now_scheduler.equals("SCAN")) {
                        System.arraycopy(arr_temp, 0, arr, 0, arr.length);
                        if(now_direction.equals("오른쪽")) {
                                for (i = 1, cnt = 1; i < queue.length + 1; i++)
                                if (arr_temp[0] <= arr_temp[i]) {
                                    print[cntP++] = arr_temp[i];
                                    arr[cnt++] = arr_temp[i];
                                }
                            if ((cnt) != number&&contain(queue,300)) {
                                number++;
                                arr[cnt++] = 300;
                            }
                            for (i = 1; i < queue.length + 1; i++) {
                                if (arr_temp[0] > arr_temp[i]) {
                                    print[cntP++] = arr_temp[i];
                                    arr[cnt++] = arr_temp[i];
                                }
                            }
                        }
                        if(now_direction.equals("왼쪽")) {
                            for (i = 1, cnt = 1; i < queue.length + 1; i++)
                                if (arr_temp[0] > arr_temp[i]) {
                                    print[cntP++] = arr_temp[i];
                                    arr[cnt++] = arr_temp[i];
                                }
                            if ((cnt) != number&&contain(queue,0)) {
                                number++;
                                arr[cnt++] = 0;
                            }
                            for (i = 1; i < queue.length + 1; i++) {
                                if (arr_temp[0] < arr_temp[i]) {
                                    print[cntP++] = arr_temp[i];
                                    arr[cnt++] = arr_temp[i];
                                }
                            }
                        }
                        System.arraycopy(arr, 0, arr_temp, 0, arr.length);
                    }

                    if(now_scheduler.equals("C-SCAN")){
                        System.arraycopy(arr_temp, 0, arr, 0, arr.length);
                        if(now_direction.equals("오른쪽")) {
                            for (i = 1, cnt = 1; i < queue.length + 1; i++)
                                if (arr_temp[0] < arr_temp[i]) {
                                    print[cntP++] = arr_temp[i];
                                    arr[cnt++] = arr_temp[i];
                                }

                            if ((cnt) != number) {
                                if(contain(queue,300)) {
                                    arr[cnt++] = 300;
                                    number++;
                                }
                                if(contain(queue,0)) {
                                    arr[cnt++] = 0;
                                    number++;
                                }
                            }
                            for (i = queue.length; i >=0; i--) {
                                if (arr_temp[0] > arr_temp[i]) {
                                    print[cntP++] = arr_temp[i];
                                    arr[cnt++] = arr_temp[i];
                                }
                            }
                        }
                        if(now_direction.equals("왼쪽")) {
                            for (i = 1, cnt = 1; i < queue.length + 1; i++)
                                if (arr_temp[0] > arr_temp[i]) {
                                    print[cntP++] = arr_temp[i];
                                    arr[cnt++] = arr_temp[i];
                                }

                            if ((cnt) != number) {
                                if(contain(queue,0)) {
                                    number++;
                                    arr[cnt++] = 0;
                                }
                                if(contain(queue,300)) {
                                    number++;
                                    arr[cnt++] = 300;
                                }
                            }
                            for (i = queue.length; i >=0; i--) {
                                if (arr_temp[0] < arr_temp[i]) {
                                    print[cntP++] = arr_temp[i];
                                    arr[cnt++] = arr_temp[i];
                                }
                            }
                        }
                        System.arraycopy(arr, 0, arr_temp, 0, arr.length);
                    }
                    if(now_scheduler.equals("LOOK")){
                        System.arraycopy(arr_temp, 0, arr, 0, arr.length);
                        if(now_direction.equals("오른쪽")) {
                            for (i = 1, cnt = 1; i < queue.length + 1; i++)
                                if (arr_temp[0] < arr_temp[i]) {
                                    print[cntP++] = arr_temp[i];
                                    arr[cnt++] = arr_temp[i];
                                }
                            for (i = 1; i < queue.length + 1; i++) {
                                if (arr_temp[0] > arr_temp[i]) {
                                    print[cntP++] = arr_temp[i];
                                    arr[cnt++] = arr_temp[i];
                                }
                            }
                        }
                        if(now_direction.equals("왼쪽")) {
                            for (i = 1, cnt = 1; i < queue.length + 1; i++)
                                if (arr_temp[0] > arr_temp[i]) {
                                    print[cntP++] = arr_temp[i];
                                    arr[cnt++] = arr_temp[i];
                                }
                            for (i = 1; i < queue.length + 1; i++) {
                                if (arr_temp[0] < arr_temp[i]) {
                                    print[cntP++] = arr_temp[i];
                                    arr[cnt++] = arr_temp[i];
                                }
                            }
                        }
                        System.arraycopy(arr, 0, arr_temp, 0, arr.length);
                    }
            if(now_scheduler.equals("C-LOOK")){
                System.arraycopy(arr_temp, 0, arr, 0, arr.length);
                if(now_direction.equals("오른쪽")) {
                    for (i = 1, cnt = 1; i < queue.length + 1; i++)
                        if (arr_temp[0] < arr_temp[i]) {
                            print[cntP++] = arr_temp[i];
                            arr[cnt++] = arr_temp[i];
                        }

                    for (i = queue.length; i >=0; i--) {
                        if (arr_temp[0] > arr_temp[i]) {
                            print[cntP++] = arr_temp[i];
                            arr[cnt++] = arr_temp[i];
                        }
                    }
                }
                if(now_direction.equals("왼쪽")) {
                    for (i = 1, cnt = 1; i < queue.length + 1; i++)
                        if (arr_temp[0] > arr_temp[i]) {
                            print[cntP++] = arr_temp[i];
                            arr[cnt++] = arr_temp[i];
                        }

                    for (i = queue.length; i >=0; i--) {
                        if (arr_temp[0] < arr_temp[i]) {
                            print[cntP++] = arr_temp[i];
                            arr[cnt++] = arr_temp[i];
                        }
                    }
                }
                System.arraycopy(arr, 0, arr_temp, 0, arr.length);
            }
            //System.arraycopy(arr_temp, 0, print, 0, arr.length);
            for (i = 0; i<number; i++)
                series.getData().add(new XYChart.Data<Number, Number>(arr_temp[i], number-i));
                    lineCT.getData().add(series);
                    total_move.setText(String.valueOf(cal_Distance(arr_temp,number)));
                    mk_list(print);
        }
    }
    private void mk_list(int list[])
    {
        ObservableList<Column> myList = FXCollections.observableArrayList();
        for (int i = 1;i<queue.length+1; i++) {
            myList.add(new Column(new SimpleIntegerProperty(i), new SimpleIntegerProperty(list[i])));
        }
        Table_num.setCellValueFactory(cellData -> cellData.getValue().orderProperty().asObject());
        Table_order.setCellValueFactory(cellData -> cellData.getValue().spotProperty().asObject());
        Table.setItems(myList);
    }

    private int cal_Distance(int[] list,int number)
    {
        int distance = 0;
        for(int i = 1;i<number;i++)
            distance += Math.abs(list[i]-list[i-1]);
        return distance;
    }
    private boolean contain(int arr[],int num)
    {
        for(int i = 0;i<arr.length;i++)
        {
            if(arr[i]==num)
                return false;
        }
        return true;

    }
}