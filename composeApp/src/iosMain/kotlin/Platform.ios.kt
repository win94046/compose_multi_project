
class IOSPlatform: Platform {
    override val name: String = "ios"
}

actual fun getPlatform(): Platform = IOSPlatform()