package com.cegep.saporiitaliano.product;

import com.cegep.saporiitaliano.common.OnItemClickListener;

public interface ProductItemClickListener<T> extends OnItemClickListener<T> {

    void onDeleteIconClicked(T item, int position);

}
