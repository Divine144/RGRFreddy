package dev._100media.rgrfreddy.client.util;

public interface RemotePlayerExtension {
    int getLerpSteps();
    void setLerpSteps(int lerpSteps);
    int getLerpHeadSteps();
    void setLerpHeadSteps(int lerpHeadSteps);
    int getLerpDeltaMovementSteps();
    void setLerpDeltaMovementSteps(int lerpDeltaMovementSteps);
    double getLerpX();
    double getLerpY();
    double getLerpZ();
}
