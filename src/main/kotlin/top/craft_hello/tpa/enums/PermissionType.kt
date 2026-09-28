package top.craft_hello.tpa.enums

import org.bukkit.command.CommandSender

enum class PermissionType(val permissionName: String) {
    TPA("bxtpa.tpa"),
    TP_HERE("bxtpa.tphere")
    ;

    companion object {
        fun hasPermission(sender: CommandSender, permissionType: PermissionType): Boolean =
            sender.hasPermission(permissionType.permissionName)
    }
}
