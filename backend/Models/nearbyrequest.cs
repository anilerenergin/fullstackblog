public class NearbySearchRequest
{
    public string[] includedTypes { get; set; }
    public int maxResultCount { get; set; }
    public LocationRestriction locationRestriction { get; set; }
}

public class LocationRestriction
{
    public Circle circle { get; set; }
}

public class Circle
{
    public Center center { get; set; }
    public double radius { get; set; }
}

public class Center
{
    public double latitude { get; set; }
    public double longitude { get; set; }
}
