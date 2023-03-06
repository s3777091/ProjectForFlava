package Function;

import Model.FeedItem;

import java.util.Comparator;
/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: 01/09/2021
  Author: team Flava
  Last modified date: 17/09/2021
  Author: members of team Flava
  Acknowledgement: in Document file
*/

//Comparator to find earlier date
public class FeedComparator implements Comparator<FeedItem> {
    @Override
    public int compare(FeedItem item1, FeedItem item2) {
        return item1.getTime().compareTo(item2.getTime());
    }
}

