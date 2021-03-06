package org.molgenis.data.rest;

import java.util.List;
import java.util.Map;

import org.molgenis.data.EntityMetaData;
import org.molgenis.security.core.MolgenisPermissionService;

public class EntityCollectionResponse
{
	private final String href;
	private final EntityMetaDataResponse meta;
	private final int start;
	private final int num;
	private final long total;
	private final String prevHref;
	private final String nextHref;
	private final List<Map<String, Object>> items;

	public EntityCollectionResponse(EntityPager entityPager, List<Map<String, Object>> items, String href,
			EntityMetaData meta, MolgenisPermissionService permissionService)
	{
		this.href = href;
		this.meta = meta != null ? new EntityMetaDataResponse(meta, permissionService) : null;
		this.start = entityPager.getStart();
		this.num = entityPager.getNum();
		this.total = entityPager.getTotal();

		Integer prevStart = entityPager.getPrevStart();
		this.prevHref = prevStart != null ? this.href + "?start=" + prevStart + "&num=" + this.num : null;

		Integer nextStart = entityPager.getNextStart();
		this.nextHref = nextStart != null ? this.href + "?start=" + nextStart + "&num=" + this.num : null;

		this.items = items;
	}

	public String getHref()
	{
		return href;
	}

	public EntityMetaDataResponse getMeta()
	{
		return meta;
	}

	public int getStart()
	{
		return start;
	}

	public int getNum()
	{
		return num;
	}

	public long getTotal()
	{
		return total;
	}

	public String getPrevHref()
	{
		return prevHref;
	}

	public String getNextHref()
	{
		return nextHref;
	}

	public List<Map<String, Object>> getItems()
	{
		return items;
	}

}