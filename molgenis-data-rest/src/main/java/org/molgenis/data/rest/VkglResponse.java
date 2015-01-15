package org.molgenis.data.rest;

import java.util.List;

public class VkglResponse
{
	private List<VkglResult> results;
	private VkglResponseMetadata metadata;
	
	public List<VkglResult> getResults()
	{
		return results;
	}
	public void setResults(List<VkglResult> results)
	{
		this.results = results;
	}
	public VkglResponseMetadata getMetadata()
	{
		return metadata;
	}
	public void setMetadata(VkglResponseMetadata metadata)
	{
		this.metadata = metadata;
	}

}
