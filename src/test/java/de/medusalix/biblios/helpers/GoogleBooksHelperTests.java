package de.medusalix.biblios.helpers;

import de.medusalix.biblios.pojos.GoogleBook;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GoogleBooksHelperTests
{
    @Test
    private void volumeInfoShouldBeCorrect()
    {
        GoogleBook.VolumeInfo volumeInfo = GoogleBooksHelper.getVolumeInfoFromIsbn("3836228734");

        Assert.assertNotNull(volumeInfo);
        Assert.assertEquals(volumeInfo.getTitle(), "Java ist auch eine Insel");
        Assert.assertEquals(volumeInfo.getAuthors(), "Christian Ullenboom");
        Assert.assertEquals(volumeInfo.getPublisher(), "Galileo Press");
        Assert.assertEquals(volumeInfo.getPublishedDate(), "2014");
    }
}
