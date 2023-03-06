package Adapter;
import Model.FeedItem;
import Thread.*;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: 10/08/2021
  Author: team Flava
  Last modified date: 17/09/2021
  Author: members of team Flava
  Acknowledgement: in Document file
*/
public class Apdater extends CoreFunction{
    public List<FeedItem> data;

    @FXML
    private Line lineTrashTheThao;

    @FXML
    private Line lineTrashCovid;

    @FXML
    private Line lineTrashPhapLuat;

    @FXML
    private Line lineTrashKinhDoanh;

    @FXML
    private Line lineTrashCongNghe;

    @FXML
    private Line lineTrashSucKhoe;

    @FXML
    private Line lineTrashGiaiTri;

    @FXML
    private Line lineTrashTheGioi;

    @FXML
    private Line lineTrashKhac;

    @FXML
    private Line lineNewest;

    @FXML
    public GridPane NewsGrid;



    //Function to clear the news catalog
    public void clearStuff() {
        NewsGrid.getChildren().clear();
        System.gc();
        System.runFinalization();
    }

    //animaiton to hanlde the fade out menu line
    public static void Line_animation_fade_out(Line fx_id) {
        FadeTransition line = new FadeTransition();
        line.setNode(fx_id);
        line.setInterpolator(Interpolator.EASE_BOTH);
        line.setToValue(0);
        line.play();
    }
//line trasition make it moving down
    public static void line_transition(Line fx_id) {
        fx_id.setOpacity(0);
        FadeTransition line = new FadeTransition();
        line.setInterpolator(Interpolator.EASE_BOTH);
        line.setToValue(1);

        TranslateTransition trans = new TranslateTransition();
        trans.setFromX(0);
        trans.setToX(5);
        trans.setInterpolator(Interpolator.EASE_BOTH);

        TranslateTransition transBack = new TranslateTransition();
        transBack.setFromX(5);
        transBack.setToX(0);
        transBack.setInterpolator(Interpolator.EASE_BOTH);

        ParallelTransition pr = new ParallelTransition(fx_id, line, trans, transBack);
        pr.play();
    }

    //make sure everyg animation remove while change tab
    public void removeAnimation() {
        HashSet<Line> animation_List = new HashSet<>();
        animation_List.add(lineTrashTheThao);
        animation_List.add(lineTrashCovid);
        animation_List.add(lineTrashPhapLuat);
        animation_List.add(lineTrashKinhDoanh);
        animation_List.add(lineTrashCongNghe);
        animation_List.add(lineTrashSucKhoe);
        animation_List.add(lineTrashGiaiTri);
        animation_List.add(lineTrashTheGioi);
        animation_List.add(lineNewest);
        animation_List.add(lineTrashKhac);
        for (Line line : animation_List) {
            Line_animation_fade_out(line);
        }
    }

    public List<FeedItem> getDataScraper() {
        return data;
    }
    //Paging functions when the page number buttona are pressed
    public void PG1()
    {
        clearStuff();
        List<FeedItem> datasplit = new ArrayList<>();
        for(int i = 0; i < 30; i ++)
        {
            datasplit.add(data.get(i));
        }
        st(datasplit);
    }
    public void PG2()
    {
        clearStuff();
        List<FeedItem> datasplit = new ArrayList<>();
        for(int i = 30; i < 60; i ++)
        {
            datasplit.add(data.get(i));
        }
        st(datasplit);
    }
    public void PG3()
    {
        clearStuff();
        List<FeedItem> datasplit = new ArrayList<>();
        for(int i = 60; i < 90; i ++)
        {
            datasplit.add(data.get(i));
        }
        st(datasplit);
    }
    public void PG4()
    {
        clearStuff();
        List<FeedItem> datasplit = new ArrayList<>();
        for(int i = 90; i < 120; i ++)
        {
            datasplit.add(data.get(i));
        }
        st(datasplit);
    }
    public void PG5()
    {
        clearStuff();
        List<FeedItem> datasplit = new ArrayList<>();
        for(int i = 120; i < data.size(); i ++)
        {
            datasplit.add(data.get(i));
        }
        st(datasplit);
    }

    //call the thread
    private TheThao th;

    private Chinhtri ct;

    private CongNghe cn;

    private Covid co;

    private Giaitri gt;

    private KinhTe kt;

    private SucKhoe sk;

    private TheGioi tg;

    private Other ot;

    private Newest ne;
    //Category functions that scrapes its respective category and then display it

    public void Newest() {
        ne = Newest.getInstance();
        clearStuff();
        //clear stuff is the function remove all cache in gridpane and memory heat
        try {
            //using multithread
            Thread thread = new Thread(() -> ne.start());
            thread.start();
            thread.join();
            data = ne.getListItem();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        removeAnimation();
        line_transition(lineNewest);
        PG1();
    }

    public void Thethao() {
        th = TheThao.getInstance();
        clearStuff();
        removeAnimation();
        try {
            Thread thread = new Thread(() -> th.start());
            thread.start();
            thread.join();
            data = th.getListItem();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        removeAnimation();
        line_transition(lineTrashTheThao);
        PG1();
    }

    public void Covid() {
        co = Covid.getInstance();
        clearStuff();
        removeAnimation();
        try {
            Thread thread = new Thread(() -> co.start());
            thread.start();
            thread.join();
            data = co.getListItem();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        removeAnimation();
        line_transition(lineTrashCovid);
        PG1();
    }

    public void Politics() {
        ct = Chinhtri.getInstance();
        clearStuff();
        removeAnimation();
        try {
            Thread thread = new Thread(() -> ct.start());
            thread.start();
            thread.join();
            data = ct.getListItem();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        removeAnimation();
        line_transition(lineTrashPhapLuat);
        PG1();
    }

    public void Business() {
        kt = KinhTe.getInstance();
        clearStuff();
        try {
            Thread thread = new Thread(() -> kt.start());
            thread.start();
            thread.join();
            data = kt.getListItem();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        removeAnimation();
        line_transition(lineTrashKinhDoanh);
        PG1();
    }

    public void Technology() {
        cn = CongNghe.getInstance();
        clearStuff();
        try {
            Thread thread = new Thread(() -> cn.start());
            thread.start();
            thread.join();
            data = cn.getListItem();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        removeAnimation();
        line_transition(lineTrashCongNghe);
        PG1();
    }

    public void Health() {
        sk = SucKhoe.getInstance();
        clearStuff();
        try {
            Thread thread = new Thread(() -> sk.start());
            thread.start();
            thread.checkAccess();
            thread.join();
            data = sk.getListItem();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        removeAnimation();
        line_transition(lineTrashSucKhoe);
        PG1();
    }

    public void Entertainment() {
        gt = Giaitri.getInstance();
        clearStuff();
        try{
            Thread thread = new Thread(() -> gt.start());
            thread.start();
            thread.join();
            data = gt.getListItem();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        removeAnimation();
        line_transition(lineTrashGiaiTri);
        PG1();
    }

    public void World() {
        tg = TheGioi.getInstance();
        clearStuff();
        try{
            Thread thread = new Thread(() -> tg.start());
            thread.start();
            thread.join();
            data = tg.getListItem();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        removeAnimation();
        line_transition(lineTrashTheGioi);
        PG1();
    }

    public void Other() {
        ot = Other.getInstance();
        clearStuff();
        try{
            Thread thread = new Thread(() -> ot.start());
            thread.start();
            thread.join();
            data = ot.getListItem();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        removeAnimation();
        line_transition(lineTrashKhac);
        PG1();
    }
}