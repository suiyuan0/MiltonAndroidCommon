
package com.milton.common.demo.fragment;


import com.milton.common.demo.R;
import com.milton.common.widget.SeatTable;

public class Fragment3SeatTable extends BaseFragment {
    public SeatTable seatTableView;

    @Override
    protected int getContentView() {
        return R.layout.fragment3_seat_table;
    }

    @Override
    protected void initView() {
        super.initView();
        seatTableView = (SeatTable) rootView.findViewById(R.id.seatView);
        seatTableView.setScreenName("8号厅荧幕");//设置屏幕名称
        seatTableView.setMaxSelected(3);//设置最多选中

        seatTableView.setSeatChecker(new SeatTable.SeatChecker() {

            @Override
            public boolean isValidSeat(int row, int column) {
                if (column == 2) {
                    return false;
                }
                return true;
            }

            @Override
            public boolean isSold(int row, int column) {
                if (row == 6 && column == 6) {
                    return true;
                }
                return false;
            }

            @Override
            public void checked(int row, int column) {

            }

            @Override
            public void unCheck(int row, int column) {

            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }

        });
        seatTableView.setData(10, 15);
    }


}
