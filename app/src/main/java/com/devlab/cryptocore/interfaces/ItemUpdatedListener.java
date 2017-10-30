package com.devlab.cryptocore.interfaces;

import java.util.Date;

/**
 * Created by shaibu on 10/30/17.
 */

public interface ItemUpdatedListener {
    void onPriceChanged(double old,double recent);
    void onDateCreatedChanged(Date old, Date recent);
    void onDateUpdatedChanged(Date old,Date recent);
    void onSourceUpdatedChanged(Date old,Date recent);
}
