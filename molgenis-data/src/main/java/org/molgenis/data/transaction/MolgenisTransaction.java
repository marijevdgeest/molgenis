package org.molgenis.data.transaction;

/**
 * Class representing a transaction in Molgenis.
 * 
 * Wraps the JpaTransaction object because JPA needs a JpaTransaction to work
 */
public class MolgenisTransaction
{
	private String id;
	private Object jpaTransaction;

	public MolgenisTransaction(String id, Object jpaTransaction)
	{
		this.id = id;
		this.jpaTransaction = jpaTransaction;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public Object getJpaTransaction()
	{
		return jpaTransaction;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		MolgenisTransaction other = (MolgenisTransaction) obj;
		if (id == null)
		{
			if (other.id != null) return false;
		}
		else if (!id.equals(other.id)) return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "MolgenisTransaction [id=" + id + "]";
	}

}
