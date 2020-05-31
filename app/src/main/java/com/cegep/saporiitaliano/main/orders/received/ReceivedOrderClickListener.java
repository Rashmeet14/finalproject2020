package com.cegep.saporiitaliano.main.orders.received;

import com.cegep.saporiitaliano.common.OnItemClickListener;

public interface ReceivedOrderClickListener<T> extends OnItemClickListener<T> {

    void onAcceptButtonClicked(T item, int position);

}
