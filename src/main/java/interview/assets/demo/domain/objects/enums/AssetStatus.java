package interview.assets.demo.domain.objects.enums;

public enum AssetStatus {
  PENDING(0),
  PROCESSED(1),
  CANCELED(2);

  private final int value;

  AssetStatus(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static AssetStatus fromValue(int value) {
    for (AssetStatus status : AssetStatus.values()) {
      if (status.getValue() == value) {
        return status;
      }
    }
    throw new IllegalArgumentException("Invalid status value: " + value);
  }
}
