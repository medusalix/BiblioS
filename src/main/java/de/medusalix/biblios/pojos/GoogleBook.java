package de.medusalix.biblios.pojos;

import java.util.List;

public class GoogleBook
{
	private String selfLink;

	private VolumeInfo volumeInfo;
	
	public class VolumeInfo
	{
		private String title, subTitle;

		private List<String> authors;
		
		private String publisher, publishedDate;

		public String getTitle()
		{
			return subTitle != null ? String.join(" ", title, subTitle) : title;
		}

		public String getAuthors()
		{
			return authors != null ? String.join(", ", authors) : "";
		}

		public String getPublisher()
		{
			return publisher != null ? publisher : "";
		}

		public String getPublishedDate()
		{
			return publishedDate != null ? publishedDate.substring(0, 4) : "";
		}
    }

    public String getSelfLink()
    {
        return selfLink;
    }

    public VolumeInfo getVolumeInfo()
	{
		return volumeInfo;
	}
}
